package com.team6.ecommerce.config.populator;

import com.team6.ecommerce.order.Order;
import com.team6.ecommerce.order.OrderRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@AllArgsConstructor
@Component
public class OrderPopulator {

    private final OrderRepository orderRepo;

    @PostConstruct
    public void init() {
        log.info("[OrderPopulator] Starting order population.");


        //orderRepo.deleteAll();
        //log.info("[OrderPopulator] Cleared Order collection.");

    }


}

