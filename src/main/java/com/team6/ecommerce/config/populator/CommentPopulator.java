package com.team6.ecommerce.config.populator;

import com.github.javafaker.Faker;

import com.team6.ecommerce.comment.Comment;
import com.team6.ecommerce.user.User;
import com.team6.ecommerce.user.UserRepository;
import com.team6.ecommerce.comment.CommentService;
import com.team6.ecommerce.comment.CommentRepository;
import com.team6.ecommerce.product.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Log4j2
@AllArgsConstructor
@Component
@DependsOn({"dataPopulator"})
public class CommentPopulator {

    private final CommentRepository commentRepo;
    private final UserRepository userRepository;
    private final Faker fake = new Faker();

    @PostConstruct
    public void init() {
        log.info("[CommentPopulator] Clearing Comment Collection.");
        commentRepo.deleteAll();

        List<User> users = userRepository.findAll();

        List<Comment> comments = Arrays.asList(
                new Comment(null, "1", users.get(0).getId(), "Amazing product! Worth every penny.", true, LocalDateTime.now()),
                new Comment(null, "2", users.get(1).getId(), "The quality is not as expected, but still decent.", true, LocalDateTime.now()),
                new Comment(null, "3", users.get(2).getId(), "Highly recommend this to everyone.", true, LocalDateTime.now()),
                new Comment(null, "4", users.get(3).getId(), "Satisfactory performance for the price.", true, LocalDateTime.now()),
                new Comment(null, "5", users.get(4).getId(), "Not happy with the purchase, avoid if possible.", false, LocalDateTime.now()),
                new Comment(null, "6", users.get(5).getId(), "Great value for money, very happy!", true, LocalDateTime.now())
        );

        commentRepo.saveAll(comments);
        log.info("[CommentPopulator] Successfully populated {} comments.", comments.size());
    }
}
