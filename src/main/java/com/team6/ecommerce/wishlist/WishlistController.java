package com.team6.ecommerce.wishlist;

import com.team6.ecommerce.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;


    @GetMapping
    public ResponseEntity<Wishlist> getWishlist() {
        String userId = getAuthenticatedUserId();

        Wishlist wishlist = wishlistService.getWishlist(userId);
        log.info("[WishlistController][getWishlist] Wishlist retrieved for user ID: {}", userId);

        return ResponseEntity.ok(wishlist);
    }


    @PostMapping("/add")
    public ResponseEntity<String> addItemToWishlist(@RequestParam String productId) {
        String userId = getAuthenticatedUserId();

        wishlistService.addItemToWishlist(userId, productId);
        log.info("[WishlistController][addItemToWishlist] Product ID: {} added to wishlist for user ID: {}", productId, userId);

        return ResponseEntity.ok("Product added to wishlist.");
    }


    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItemFromWishlist(@RequestParam String productId) {
        String userId = getAuthenticatedUserId();

        wishlistService.removeItemFromWishlist(userId, productId);
        log.info("[WishlistController][removeItemFromWishlist] Product ID: {} removed from wishlist for user ID: {}", productId, userId);

        return ResponseEntity.ok("Product removed from wishlist.");
    }


    @DeleteMapping("/reset")
    public ResponseEntity<String> resetWishlist() {
        String userId = getAuthenticatedUserId();

        wishlistService.resetWishlist(userId);
        log.info("[WishlistController][resetWishlist] Wishlist reset for user ID: {}", userId);

        return ResponseEntity.ok("Wishlist has been reset.");
    }


    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            log.error("[WishlistController] Unauthorized access attempt.");
            throw new IllegalStateException("User is not authenticated.");
        }

        User user = (User) authentication.getPrincipal();
        log.info("[WishlistController][getAuthenticatedUserId] User ID retrieved: {}", user.getId());

        return user.getId();
    }
}
