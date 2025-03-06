package com.team6.ecommerce.distributor;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributorRepository extends MongoRepository<Distributor, String> {

}
