package com.team6.ecommerce.config.populator;

import com.github.javafaker.Faker;
import com.team6.ecommerce.rating.Rating;
import com.team6.ecommerce.rating.RatingRepository;
import com.team6.ecommerce.user.User;
import com.team6.ecommerce.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Log4j2
@AllArgsConstructor
@Component
@DependsOn({"dataPopulator"})
public class RatingPopulator {

    private final RatingRepository ratingRepo;
    private final UserRepository userRepository;
    private final Faker fake = new Faker();
    private final Random random = new Random();

    @PostConstruct
    public void init() {
        log.info("[RatingPopulator] Clearing Rating Collection.");
        ratingRepo.deleteAll();

        List<User> users = userRepository.findAll();
        List<Rating> ratings = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            ratings.add(new Rating(
                    null,
                    String.valueOf(random.nextInt(10) + 1), // Random productId between 1 and 10
                    users.get(random.nextInt(users.size())).getId(),
                    random.nextInt(5) + 1 // Random rating between 1 and 5
            ));
        }

        ratingRepo.saveAll(ratings);
        log.info("[RatingPopulator] Successfully populated {} ratings.", ratings.size());
    }
}
