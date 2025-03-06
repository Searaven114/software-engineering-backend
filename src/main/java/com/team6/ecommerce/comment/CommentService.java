package com.team6.ecommerce.comment;

import com.team6.ecommerce.order.OrderService;
import com.team6.ecommerce.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final OrderService orderService;
    private final UserRepository userRepo;


    @Transactional
    public String addComment(String userId, String productId, String content) {
        validatePurchase(userId, productId);

        Comment comment = new Comment();
        comment.setProductId(productId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setApproved(false); // Comments need approval
        comment.setCreatedDate(LocalDateTime.now());
        commentRepo.save(comment);

        log.info("[CommentService][addComment] Comment added: {}", comment);
        return "Comment submitted for approval.";
    }


    @Transactional
    public void approveComment(String commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setApproved(true);
        commentRepo.save(comment);
        log.info("[CommentService][approveComment] Comment ID: {} approved successfully.", commentId);
    }


    public List<Comment> getUnapprovedComments() {
        List<Comment> unapprovedComments = commentRepo.findByApprovedFalse();
        log.info("[CommentService][getUnapprovedComments] Retrieved {} unapproved comments.", unapprovedComments.size());
        return unapprovedComments;
    }


    public List<Comment> getAllCommentsForProduct(String productId) {
        List<Comment> comments = commentRepo.findByProductId(productId);
        log.info("[CommentService][getAllCommentsForProduct] Retrieved {} comments for product ID: {}", comments.size(), productId);
        return comments;
    }


    public List<Comment> getApprovedComments(String productId) {
        List<Comment> approvedComments = commentRepo.findByProductIdAndApprovedTrue(productId);
        log.info("[CommentService][getApprovedComments] Retrieved {} approved comments for product ID: {}", approvedComments.size(), productId);
        return approvedComments;
    }


    private void validatePurchase(String userId, String productId) {
        boolean hasPurchased = orderService.fetchOrdersByUserId(userId).stream()
                .flatMap(order -> order.getCart().getCartItems().stream())
                .anyMatch(cartItem -> cartItem.getProduct().getId().equals(productId));

        if (!hasPurchased) {
            throw new RuntimeException("User has not purchased this product.");
        }
    }
}
