package com.team6.ecommerce.orderitem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor
@Setter
@Getter
@Document(collection = "orderitem")
public class OrderItem {

    private String id;
    private int quantity;
    private Double price;


}

