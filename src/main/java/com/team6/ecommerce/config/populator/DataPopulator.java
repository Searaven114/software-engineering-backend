package com.team6.ecommerce.config.populator;

import com.github.javafaker.Faker;
import com.team6.ecommerce.address.Address;
import com.team6.ecommerce.cart.Cart;
import com.team6.ecommerce.cart.CartRepository;
import com.team6.ecommerce.cartitem.CartItem;
import com.team6.ecommerce.product.Product;
import com.team6.ecommerce.product.ProductRepository;
import com.team6.ecommerce.user.User;
import com.team6.ecommerce.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@AllArgsConstructor
@Component
@DependsOn({"productPopulator"})
public class DataPopulator {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final CartRepository cartRepo;
    private final BCryptPasswordEncoder encoder;

    private final Faker fake = new Faker();

    @PostConstruct
    public void init() {
        log.info("[DataPopulator] Starting data population.");

        // Clear collections to avoid duplicates
        userRepo.deleteAll();
        cartRepo.deleteAll();
        log.info("[DataPopulator] Cleared User and Cart collections.");

        // Step 1: Populate users
        List<User> users = populateUsers();

        // Step 2: Assign carts to users
        assignCartsToUsers(users);
    }

    private List<User> populateUsers() {
        log.info("[DataPopulator] Populating users.");

        List<User> users = Arrays.asList(
                new User("fuat", encoder.encode("avni"), "${receiver-test-email}", "05665127700"),
                new User("admin", encoder.encode("adminpw"), "admin@example.com", fake.phoneNumber().phoneNumber()),
                new User("salesmanager", encoder.encode("salespw"), "sales@example.com", fake.phoneNumber().phoneNumber()),
                new User("productmanager", encoder.encode("productpw"), "product@example.com", fake.phoneNumber().phoneNumber()),
                new User("customer1", encoder.encode("customerpw"), "customer1@example.com", fake.phoneNumber().phoneNumber()),
                new User("customer2", encoder.encode("customerpw"), "customer2@example.com", fake.phoneNumber().phoneNumber()),
                new User("customer3", encoder.encode("customerpw"), "testemail@example.com", fake.phoneNumber().phoneNumber())
        );

        for (User user : users) {
            Set<String> roles = new HashSet<>();

            if ("admin@example.com".equals(user.getUsername())) {
                roles.add("ROLE_ADMIN");
                roles.add("ROLE_SALESMANAGER");
                roles.add("ROLE_PRODUCTMANAGER");
                roles.add("ROLE_CUSTOMER");
            } else if ("sales@example.com".equals(user.getUsername())) {
                roles.add("ROLE_SALESMANAGER");
                roles.add("ROLE_CUSTOMER");
            } else if ("product@example.com".equals(user.getUsername())) {
                roles.add("ROLE_PRODUCTMANAGER");
                roles.add("ROLE_CUSTOMER");
            } else {
                roles.add("ROLE_CUSTOMER");
            }
            user.setRoles(roles);

            user.setRegisterDate(LocalDateTime.now().toString());
            user.setRegisterIp(fake.internet().ipV4Address());
            user.setTaxId(fake.idNumber().valid());
            user.setAge(fake.number().digits(2));

            List<Address> addresses = generateMockAddresses();
            user.setAddresses(addresses);
        }

        userRepo.saveAll(users);
        log.info("[DataPopulator] Saved users: {}", users.size());

        return users;
    }

    private List<Address> generateMockAddresses() {
        List<Address> addresses = new ArrayList<>();
        int number = fake.number().numberBetween(1, 10);

        if (number >= 4) {
            addresses.add(new Address("asd", "asd", "asd", "asd"));
        } else if (number < 3) {
            addresses.add(new Address("asd", "asd", "asd", "asd"));
            addresses.add(new Address("asd", "asd", "asd", "asd"));
        } else {
            addresses.add(new Address("asd", "asd", "asd", "asd"));
        }

        return addresses;
    }

    private void assignCartsToUsers(List<User> users) {
        log.info("[DataPopulator] Assigning carts to users.");

        for (User user : users) {
            List<CartItem> cartItems = generateCartItemsForUser(user.getUsername());

            Cart cart = new Cart(user.getId(), cartItems);
            cartRepo.save(cart);

            user.setCartId(cart.getId());
            userRepo.save(user);

            log.info("[DataPopulator] Assigned cart with ID {} to user {}", cart.getId(), user.getEmail());
        }
    }

    private List<CartItem> generateCartItemsForUser(String username) {
        List<CartItem> cartItems = new ArrayList<>();

        Map<String, List<String>> userProductMap = Map.of(
                "customer1@example.com", List.of("1", "2"),
                "customer2@example.com", List.of("3", "4"),
                "testemail@example.com", List.of("5")
        );

        Map<String, List<Integer>> userQuantityMap = Map.of(
                "customer1@example.com", List.of(2, 1),
                "customer2@example.com", List.of(7, 99),
                "testemail@example.com", List.of(1)
        );

        List<String> productIds = userProductMap.getOrDefault(username, Collections.emptyList());
        List<Integer> quantities = userQuantityMap.getOrDefault(username, Collections.emptyList());

        for (int i = 0; i < productIds.size(); i++) {

            String productId = productIds.get(i);
            int quantity = quantities.get(i);

            Optional<Product> productOpt = productRepo.findById(productId);
            if (productOpt.isPresent()) {
                cartItems.add(new CartItem(productOpt.get(), quantity));
            } else {
                log.warn("[DataPopulator] Product with ID {} not found!", productId);
            }
        }

        return cartItems;
    }
}
