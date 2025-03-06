package com.team6.ecommerce.comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByProductIdAndApprovedTrue(String productId); // Fetch approved comments

    List<Comment> findByProductId(String productId); // Fetch all comments for admin

    boolean existsByProductIdAndUserId(String productId, String userId); // Check if user already commented

    List<Comment> findByApprovedFalse();
}
