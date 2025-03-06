package com.team6.ecommerce.category;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {


    List<Category> findByIsActiveTrue();

    Boolean existsByName(String name);

    Optional<Category> findByNameAndIsActiveTrue(String name);

    Optional<Category> findByName(String name);

    void deleteByName(String name);
}
