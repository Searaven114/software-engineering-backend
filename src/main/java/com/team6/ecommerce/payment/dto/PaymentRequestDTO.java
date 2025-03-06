package com.team6.ecommerce.payment.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {

    private String cardNumber;
    private String cardExpiry;
    private String cvv;
}
