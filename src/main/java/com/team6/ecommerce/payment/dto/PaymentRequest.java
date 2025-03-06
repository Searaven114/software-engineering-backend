package com.team6.ecommerce.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {

    private String cardNumber;
    private String cardExpiry;
    private String cvv;
    private Long totalAmount;

}
