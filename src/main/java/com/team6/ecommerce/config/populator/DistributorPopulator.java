package com.team6.ecommerce.config.populator;

import com.github.javafaker.Faker;
import com.team6.ecommerce.distributor.Distributor;
import com.team6.ecommerce.distributor.DistributorRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Log4j2
@AllArgsConstructor
@Component
@Order(1)
public class DistributorPopulator {

    private final DistributorRepository distributorRepo;

    private final Faker fake = new Faker();

    @PostConstruct
    public void init() {

        log.info("[DistributorPopulator] Clearing Distributor Collection.");
        distributorRepo.deleteAll();

        List<Distributor> distributors = Arrays.asList(
                new Distributor(
                        "1",
                        "Aral A.Ş",
                        fake.phoneNumber().phoneNumber(),
                        fake.address().fullAddress(),
                        fake.internet().url(),
                        true
                ),
                new Distributor(
                        "2",
                        "Bircom",
                        fake.phoneNumber().phoneNumber(),
                        fake.address().fullAddress(),
                        fake.internet().url(),
                        true
                )
        );

        distributorRepo.saveAll(distributors);

        log.info("[DistributorPopulator] Populated {} distributors.", distributors.size());
        for (Distributor distributor : distributors) {
            log.info("[DistributorPopulator] Saved Distributor: ID = {}, Name = {}", distributor.getId(), distributor.getName());
        }
    }
}
