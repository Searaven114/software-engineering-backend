package com.team6.ecommerce.config.populator;


import com.team6.ecommerce.category.Category;
import com.team6.ecommerce.category.CategoryRepository;
import com.team6.ecommerce.distributor.DistributorRepository;
import com.team6.ecommerce.product.ProductRepository;
import com.team6.ecommerce.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Component
public class CategoryPopulator {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final ProductRepository productRepo;
    private final DistributorRepository distributorRepo;
    private final CategoryRepository categoryRepo;


    @PostConstruct
    public void init() {

        categoryRepo.deleteAll();

        List<Category> categories = Arrays.asList(

                new Category("1", "Laptop", true),
                new Category("2", "Monitor", true),
                new Category("3", "Keyboard", true),
                new Category("4", "Mouse", true)
        );

        categoryRepo.saveAll(categories);

    }
}
