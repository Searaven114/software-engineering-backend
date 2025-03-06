package com.team6.ecommerce.distributor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DistributorService {

    private final DistributorRepository distributorRepo;


    public List<Distributor> findAll() {
        return distributorRepo.findAll();
    }

    public Optional<Distributor> findById(String id) {
        return distributorRepo.findById(id);
    }

    @Transactional
    public Distributor save(Distributor distributor) {
        return distributorRepo.save(distributor);
    }
    @Transactional
    public void deleteById(String id) {
        if (!distributorRepo.existsById(id)) {
            throw new RuntimeException("Distributor with ID " + id + " does not exist.");
        }
        distributorRepo.deleteById(id);
    }

}


