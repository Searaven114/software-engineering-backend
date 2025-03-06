package com.team6.ecommerce.order;

import com.team6.ecommerce.address.Address;
import com.team6.ecommerce.cart.Cart;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String userId;
    private Cart cart;
    private OrderStatus orderStatus;
    private Date createdAt;
    private Long total;
    private Address address;

    public Order(String userId, Cart cart, OrderStatus orderStatus, Date createdAt, Long total) {
        this.userId = userId;
        this.cart = cart;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.total = total;
    }

    public Order(String userId, Cart cart, OrderStatus orderStatus, Date createdAt, Long total, Address address) {
        this.userId = userId;
        this.cart = cart;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.total = total;
        this.address = address;
    }
}







