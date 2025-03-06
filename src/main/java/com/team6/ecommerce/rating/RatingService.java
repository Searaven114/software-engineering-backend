package com.team6.ecommerce.rating;

import com.team6.ecommerce.order.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@AllArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepo;
    private final OrderService orderService;

    public String addRating(String userId, String productId, int ratingValue) {
        validatePurchase(userId, productId);

        Rating rating = new Rating();
        rating.setProductId(productId);
        rating.setUserId(userId);
        rating.setRating(ratingValue);
        ratingRepo.save(rating);

        log.info("[RatingService][addRating] Rating added: {}", rating);
        return "Rating added successfully.";
    }

    public double calculateAverageRating(String productId) {
        List<Rating> ratings = ratingRepo.findByProductId(productId);
        if (ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
    }

    private void validatePurchase(String userId, String productId) {
        boolean hasPurchased = orderService.fetchOrdersByUserId(userId).stream()
                .flatMap(order -> order.getCart().getCartItems().stream())
                .anyMatch(cartItem -> cartItem.getProduct().getId().equals(productId));

        if (!hasPurchased) {
            throw new RuntimeException("User has not purchased this product.");
        }
    }
}
