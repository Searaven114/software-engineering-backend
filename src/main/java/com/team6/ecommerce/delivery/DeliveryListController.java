package com.team6.ecommerce.delivery;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/pm/deliveries")
public class DeliveryListController {

    private final DeliveryListService deliveryService;

    @Secured({"ROLE_PRODUCTMANAGER"})
    @GetMapping("/pending")
    public ResponseEntity<List<DeliveryList>> getPendingDeliveries() {
        return ResponseEntity.ok(deliveryService.getPendingDeliveries());
    }

    @Secured({"ROLE_PRODUCTMANAGER"})
    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryList> updateDeliveryStatus(
            @PathVariable String id, @RequestParam boolean isCompleted) {
        return ResponseEntity.ok(deliveryService.updateDeliveryStatus(id, isCompleted));
    }
}
