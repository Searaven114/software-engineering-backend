package com.team6.ecommerce.delivery;

import com.team6.ecommerce.address.Address;
import com.team6.ecommerce.cartitem.CartItem2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class DeliveryListService {

    private final DeliveryListRepository deliveryRepo;

    @Transactional
    public DeliveryList createDeliveryEntry(String customerId, CartItem2 cartitem2, Address address) {
        DeliveryList delivery = new DeliveryList();
        delivery.setCustomerId(customerId);
        delivery.setCartItem2(cartitem2);
        delivery.setDeliveryAddress(address);
        delivery.setCompleted(false);
        delivery.setCreatedDate(new Date());

        return deliveryRepo.save(delivery);
    }

    public List<DeliveryList> getDeliveriesByCustomerId(String customerId) {
        return deliveryRepo.findByCustomerId(customerId);
    }

    public List<DeliveryList> getPendingDeliveries() {
        return deliveryRepo.findByIsCompleted(false);
    }

    @Transactional
    public DeliveryList updateDeliveryStatus(String deliveryId, boolean isCompleted) {
        DeliveryList delivery = deliveryRepo.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        delivery.setCompleted(isCompleted); // Corrected method call
        return deliveryRepo.save(delivery);
    }
}
