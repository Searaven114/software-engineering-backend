package com.team6.ecommerce.cartitem;

import com.team6.ecommerce.product.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItem2 {

    private String productName;
    private Integer quantity;
    private Double price;
}
