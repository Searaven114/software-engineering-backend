package com.team6.ecommerce.config.populator;


import com.team6.ecommerce.delivery.DeliveryListRepository;
import com.team6.ecommerce.invoice.InvoiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@AllArgsConstructor
@Component
public class DeliveryPopulator {

    DeliveryListRepository deliveryListRepository;

    @PostConstruct
    public void init() {

        //deliveryListRepository.deleteAll();

        //log.info("[DeliveryPopulator] Cleared deliveryList collection.");


    }
}

