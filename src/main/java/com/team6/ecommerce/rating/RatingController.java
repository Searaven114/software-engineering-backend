package com.team6.ecommerce.rating;

import com.team6.ecommerce.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@AllArgsConstructor
@Log4j2
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<String> addRating(
            @RequestParam String productId,
            @RequestParam int ratingValue) {
        String userId = getAuthenticatedUserId("addRating");
        String result = ratingService.addRating(userId, productId, ratingValue);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{productId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable String productId) {
        return ResponseEntity.ok(ratingService.calculateAverageRating(productId));
    }


    private String getAuthenticatedUserId(String methodName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("[RatingController][{}] Unauthorized access attempt.", methodName);
            throw new IllegalStateException("User is not authenticated.");
        }
        if (!(authentication.getPrincipal() instanceof User)) {
            log.warn("[RatingController][{}] Invalid principal type.", methodName);
            throw new IllegalStateException("Invalid user principal.");
        }
        User user = (User) authentication.getPrincipal();
        log.info("[RatingController][{}] Authenticated user ID: {}", methodName, user.getId());
        return user.getId();
    }
}

