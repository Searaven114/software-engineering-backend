package com.team6.ecommerce.config.populator;

import com.team6.ecommerce.product.Product;
import com.team6.ecommerce.product.ProductRepository;
import com.team6.ecommerce.user.User;
import com.team6.ecommerce.user.UserRepository;
import com.team6.ecommerce.wishlist.Wishlist;
import com.team6.ecommerce.wishlist.WishlistRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.util.*;

@Log4j2
@AllArgsConstructor
@Component
@DependsOn({"productPopulator", "dataPopulator"})
public class WishlistPopulator {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;

}
