package com.team6.ecommerce.wishlist;

import com.team6.ecommerce.exception.UserNotFoundException;
import com.team6.ecommerce.user.User;
import com.team6.ecommerce.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;

    public Wishlist getWishlist(String userId) {
        log.info("[WishlistService][getWishlist] Fetching wishlist for user ID: {}", userId);

        return wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.info("[WishlistService][getWishlist] No wishlist found for user ID: {}. Creating a new one.", userId);
                    Wishlist wishlist = new Wishlist();
                    wishlist.setUserId(userId);
                    wishlist.setProductIds(new HashSet<>());
                    return wishlistRepository.save(wishlist);
                });
    }


    @Transactional
    public void addItemToWishlist(String userId, String productId) {
        validateUser(userId);

        Wishlist wishlist = getWishlist(userId);
        wishlist.getProductIds().add(productId);

        wishlistRepository.save(wishlist);
        log.info("[WishlistService][addItemToWishlist] Product ID: {} added to wishlist for user ID: {}", productId, userId);
    }


    @Transactional
    public void removeItemFromWishlist(String userId, String productId) {
        validateUser(userId);

        Wishlist wishlist = getWishlist(userId);
        boolean removed = wishlist.getProductIds().remove(productId);

        if (removed) {
            wishlistRepository.save(wishlist);
            log.info("[WishlistService][removeItemFromWishlist] Product ID: {} removed from wishlist for user ID: {}", productId, userId);
        } else {
            log.warn("[WishlistService][removeItemFromWishlist] Product ID: {} not found in wishlist for user ID: {}", productId, userId);
        }
    }

    public void resetWishlist(String userId) {
        validateUser(userId);

        Wishlist wishlist = getWishlist(userId);
        wishlist.getProductIds().clear();

        wishlistRepository.save(wishlist);
        log.info("[WishlistService][resetWishlist] Wishlist reset for user ID: {}", userId);
    }

    
    private void validateUser(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            log.error("[WishlistService][validateUser] User with ID: {} not found.", userId);
            throw new UserNotFoundException("User not found.");
        }
        //log.info("[WishlistService][validateUser] User validation successful for user ID: {}", userId);
    }

    public List<String> getUsersByProductInWishlist(String productId) {
        log.info("[WishlistService][getUsersByProductInWishlist] Fetching users for product ID: {}", productId);
        List<Wishlist> wishlists = wishlistRepository.findAll();
        List<String> userIds = wishlists.stream()
                .filter(wishlist -> wishlist.getProductIds().contains(productId))
                .map(Wishlist::getUserId)
                .toList();
        log.info("[WishlistService][getUsersByProductInWishlist] Found {} users with product ID: {} in their wishlist", userIds.size(), productId);
        return userIds;
    }
}
