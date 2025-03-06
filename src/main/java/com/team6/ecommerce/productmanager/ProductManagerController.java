package com.team6.ecommerce.productmanager;


import com.team6.ecommerce.category.Category;
import com.team6.ecommerce.category.CategoryService;
import com.team6.ecommerce.comment.Comment;
import com.team6.ecommerce.comment.CommentService;
import com.team6.ecommerce.exception.ProductNotFoundException;
import com.team6.ecommerce.product.Product;
import com.team6.ecommerce.product.ProductRepository;
import com.team6.ecommerce.product.ProductService;
import com.team6.ecommerce.product.dto.ProductDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pm")
@AllArgsConstructor
@Log4j2
public class ProductManagerController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductRepository productRepo;
    private final CommentService commentService;

    //━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━KATEGORI KISMI━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//


    //TODO bu direkt Category türünde almamalı sanırım, dto alacak sekilde bu ve servicedeki update metodu degismeli (sanıırm)
    @Secured({"ROLE_ADMIN", "ROLE_PRODUCTMANAGER"})
    @GetMapping("/update-category/{id}")
    public ResponseEntity<?> updateCategory (@PathVariable String id, @RequestBody Category category) {

        return ResponseEntity.ok().body( categoryService.update(id, category));

    }

    @Secured({"ROLE_ADMIN", "ROLE_PRODUCTMANAGER"})
    @GetMapping("/delete-category/{id}")
    public ResponseEntity<?> deleteCategory (@PathVariable String id) {

        return ResponseEntity.ok().body( categoryService.deleteCategory(id));

    }


    //━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━PRODUCT KISMI━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    @Secured({"ROLE_ADMIN", "ROLE_PRODUCTMANAGER"})
    @PostMapping(value = "/product/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProduct (@RequestBody @Valid ProductDTO productDTO) {

        try {
            Product newProduct = productService.addProduct(productDTO);

            return ResponseEntity.ok(newProduct);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    //ChangeStock endpoint
    @Secured({"ROLE_ADMIN", "ROLE_PRODUCTMANAGER"})
    @PatchMapping("/product/{id}/change-stock")
    public ResponseEntity<?> changeStock(
            @PathVariable String id,
            @RequestParam String newStock) {
        try {
            // Parse the stock value from String to int
            int stockValue = Integer.parseInt(newStock);

            // Validate stock value (ensure it's non-negative)
            if (stockValue < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Stock value cannot be negative.");
            }

            // Retrieve product by ID directly using repository
            Optional<Product> productOptional = productRepo.findById(id);
            if (productOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + id + " not found.");
            }

            // Update the stock value
            Product product = productOptional.get();
            product.setQuantityInStock(stockValue);

            // Save the updated product back to the database
            Product updatedProduct = productRepo.save(product);

            // Return the updated product
            return ResponseEntity.ok(updatedProduct);

        } catch (NumberFormatException e) {
            // Handle invalid number format
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid stock value: " + newStock + ". Please provide a valid number.");
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating stock.");
        }
    }



    @Secured({"ROLE_ADMIN", "ROLE_PRODUCTMANAGER"})
    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable String id) {
        try {
            productService.removeProduct(id);
            log.info("[ProductManagerController][removeProduct] Product with ID: {} removed successfully.", id);
            return ResponseEntity.ok("Product removed successfully.");
        } catch (ProductNotFoundException e) {
            log.error("[ProductManagerController][removeProduct] Product not found with ID: {}. {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error("[ProductManagerController][removeProduct] Error removing product with ID: {}. {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while removing the product.");
        }
    }


    //━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━COMMENT KISMI━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    @Secured({"ROLE_PRODUCTMANAGER"})
    @PatchMapping("/comments/{commentId}/approve")
    public ResponseEntity<?> approveComment(@PathVariable String commentId) {
        commentService.approveComment(commentId);
        log.info("[ProductManagerController][approveComment] Comment ID: {} approved successfully.", commentId);
        return ResponseEntity.ok("Comment approved successfully.");
    }

    @Secured({"ROLE_PRODUCTMANAGER"})
    @GetMapping("/comments/unapproved")
    public ResponseEntity<List<Comment>> listUnapprovedComments() {
        List<Comment> unapprovedComments = commentService.getUnapprovedComments();
        log.info("[ProductManagerController][listUnapprovedComments] Retrieved {} unapproved comments.", unapprovedComments.size());
        return ResponseEntity.ok(unapprovedComments);
    }

    @Secured({"ROLE_PRODUCTMANAGER"})
    @GetMapping("/comments/{productId}")
    public ResponseEntity<List<Comment>> listCommentsByProduct(@PathVariable String productId) {
        List<Comment> comments = commentService.getAllCommentsForProduct(productId);
        log.info("[ProductManagerController][listCommentsByProduct] Retrieved {} comments for product ID: {}", comments.size(), productId);
        return ResponseEntity.ok(comments);
    }







}
