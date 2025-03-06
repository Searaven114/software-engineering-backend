package com.team6.ecommerce.user;


import com.github.javafaker.Bool;
import com.team6.ecommerce.address.Address;
import com.team6.ecommerce.address.AddressDTO;
import com.team6.ecommerce.exception.UserNotFoundException;
import com.team6.ecommerce.user.dto.*;
import com.team6.ecommerce.exception.UserRegistrationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.bind.annotation.*;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepo;


    @PostMapping(value = "/user/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegistrationDTO dto) {

        String userId = getAuthenticatedUserId("registerUser");

        String response = userService.registerUser(dto);

        if (response.equals("User created successfully")) {
            return ResponseEntity.ok().body(response);
        };

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurredregisterUser");
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/user/logout")
    public ResponseEntity<?> logoutUser() {
        try {
            SecurityContextHolder.clearContext(); // Clear the current security context
            log.info("[UserController][logoutUser] User successfully logged out.");
            return ResponseEntity.ok("User successfully logged out.");
        } catch (Exception e) {
            log.error("[UserController][logoutUser] Error during logout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed.");
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/profile")
    public ResponseEntity<?> showProfile() {
        try {

            String userId = getAuthenticatedUserId("showProfile");

            ProfileDTO profile = userService.getProfile(userId);

            return ResponseEntity.ok().body(profile);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("USER NOT FOUND");
        }
    }

    //duzenlenmeli
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/address")
    public ResponseEntity<?> getUserAddress() {
        try {

            String userId = getAuthenticatedUserId("getUserAddress");

            User user = userRepo.findById(userId).orElseThrow( () -> new UserNotFoundException("User not found") );

            Address address = user.getAddresses().get(user.getAddresses().size() - 1);

            AddressDTO dto = AddressDTO.builder()
                    .street(address.getStreet())
                    .city(address.getStreet())
                    .zipCode(address.getZipCode())
                    .country(address.getCountry())
                    .notes(address.getNotes())
                    .build();

            if (address == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User has not added an address");
            }

            return ResponseEntity.ok().body(dto);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("/account/address")
    public ResponseEntity<?> addAddressToAccount(@RequestBody @Valid AddressDTO dto) {

        String userId = getAuthenticatedUserId("getUserAddress");

        User user = userRepo.findById(userId).orElseThrow( () -> new UserNotFoundException("User not found") );

        log.info("[UserController][addAddressToAccount] User {} is adding a new address", user.getEmail());

        return userService.addAddress(user.getId(), dto);
    }



    @GetMapping("/user/authcheck")
    public Boolean checkAuthStatus(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null){

            log.info("[UserController][checkAuthStatus] auth fail reported due to auth being null.");
            return false;

        }

        if (!auth.isAuthenticated()){
            log.info("[UserController][checkAuthStatus] auth fail reported due to isAuthentication being false.");
            return false;
        }

        log.info("[UserController][checkAuthStatus] auth succeeded.");
        return true;
    }


    private String getAuthenticatedUserId(String methodName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("[UserController][{}] Unauthorized access attempt.", methodName);
            throw new IllegalStateException("User is not authenticated.");
        }
        if (!(authentication.getPrincipal() instanceof User)) {
            log.warn("[UserController][{}] Invalid principal type.", methodName);
            throw new IllegalStateException("Invalid user principal.");
        }
        User user = (User) authentication.getPrincipal();
        log.info("[UserController][{}] Authenticated user ID: {}", methodName, user.getId());
        return user.getId();
    }
}
