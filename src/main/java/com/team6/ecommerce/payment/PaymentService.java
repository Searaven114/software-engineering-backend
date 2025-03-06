package com.team6.ecommerce.payment;

import com.team6.ecommerce.payment.dto.PaymentRequest;
import com.team6.ecommerce.payment.dto.PaymentResponse;
import com.team6.ecommerce.constants.Strings;
import org.springframework.stereotype.Service;


//unused
@Service
public class PaymentService {


    // Mock validation logic
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {

        if (isValidCard(paymentRequest.getCardNumber())) {
            return new PaymentResponse(true, Strings.PAYMENT_SUCCESSFUL);
        }
        return new PaymentResponse(false, Strings.PAYMENT_FAILED_INVALID_CARD_DETAILS);
    }

    private boolean isValidCard(String cardNumber) {
        //Simple mock validation
        return cardNumber != null && cardNumber.length() == 16;
    }


}
