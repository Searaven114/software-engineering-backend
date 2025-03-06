package com.team6.ecommerce.order;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    public List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    public List<Order> findAllByUserIdAndOrderStatus(String userId, OrderStatus orderStatus);

    List<Order> findAllByUserId(String userId);
}

