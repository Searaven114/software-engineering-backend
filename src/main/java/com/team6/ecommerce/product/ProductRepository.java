package com.team6.ecommerce.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

//    The user should be able to search products depending on their names or descriptions.
//    Additionally, the user should be able to sort products depending on their price or
//    popularity. If a product is out-of-stock the product must still be searchable, however the
//    user should not be able to add it to the shopping cart.

    List<Product> findByCategoryId(String categoryId);

//    // Search by title or description containing the search term, ignoring case
//    List<Product> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    // Sorting by price or popularity
    Page<Product> findAllByOrderByBasePriceAsc(Pageable pageable);
    Page<Product> findAllByOrderByPopularityPointDesc(Pageable pageable);

    // Find products by categoryId and sort by price with pagination
    Page<Product> findByCategoryId(String categoryId, Pageable pageable);


    Page<Product> findAllByPopularityPoint(Pageable pageable);

    // Paginated search by title or description containing the search term, ignoring case
    Page<Product> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);


    boolean findByTitle(String title);

    Optional<Product> findByTitleIgnoreCase(String title);

    boolean existsByTitle(String title);

    boolean existsBySerialNumber(String serialNumber);
}


