package com.team6.ecommerce.cart.dto;

import com.team6.ecommerce.cartitem.CartItem;
import com.team6.ecommerce.invoice.Invoice;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CheckoutResponseDTO {
    private Invoice invoice;
    private List<CartItem> purchasedItems;
    private Double totalAmount;
}
