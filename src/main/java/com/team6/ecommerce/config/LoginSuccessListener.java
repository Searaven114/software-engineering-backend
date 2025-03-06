package com.team6.ecommerce.config;


import com.team6.ecommerce.cart.CartService;
import com.team6.ecommerce.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Log4j2
@AllArgsConstructor
@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final CartService cartService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            log.info("[LoginSuccessListener] User {} logged in. Validating their cart.", user.getEmail());

            // Validate cart items
            String message = cartService.validateCartItems(user.getId());

            if (message != null) {
                log.info("[LoginSuccessListener] Cart validation message for user {}: {}", user.getId(), message);

            }
        }
    }
}