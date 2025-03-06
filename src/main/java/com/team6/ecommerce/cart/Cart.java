package com.team6.ecommerce.cart;

import com.team6.ecommerce.cartitem.CartItem;
import jakarta.annotation.Nonnull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    //private User user;

    private String userId;

    private List<CartItem> cartItems = new ArrayList<>();

    private Double TotalPrice;

    public Cart(String userId, List<CartItem> cartItems) {
        this.userId = userId;
        this.cartItems = cartItems;
    }
}
