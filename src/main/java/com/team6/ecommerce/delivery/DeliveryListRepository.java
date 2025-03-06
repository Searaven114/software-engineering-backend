package com.team6.ecommerce.delivery;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryListRepository extends MongoRepository<DeliveryList, String> {

    List<DeliveryList> findByCustomerId(String customerId);

    List<DeliveryList> findByIsCompleted(boolean isCompleted);
}
