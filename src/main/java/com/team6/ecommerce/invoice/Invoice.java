package com.team6.ecommerce.invoice;

import com.team6.ecommerce.cartitem.CartItem2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "invoices")
public class Invoice {
    @Id
    private String id;
    private String orderId;
    private String userId;
    private Double totalAmount;
    private Date invoiceDate;
    private String email;
    private List<CartItem2> purchasedItems;
}
