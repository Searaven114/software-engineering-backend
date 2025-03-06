package com.team6.ecommerce.product;

import com.team6.ecommerce.category.Category;
import com.team6.ecommerce.category.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepo;
    private final CategoryService categoryService;


    @GetMapping("/get-all-products")
    public ResponseEntity<?> getAllProducts(){

        List<Product> products = productService.getAllProducts();

        if ( products.isEmpty() ){
            return ResponseEntity.ok("NO PRODUCTS RETURNED");
        } else {
            return ResponseEntity.ok().body(products);
        }
    }


    @GetMapping("/get-categories")
    public ResponseEntity<?> getCategories() {

        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }


    // Fetch paginated products without sorting
    @GetMapping("/paginated")
    public ResponseEntity<Page<Product>> getPaginatedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> products = productService.getPaginatedProducts(page, size);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/sort-by-price")
    public ResponseEntity<Page<Product>> getProductsSortedByPrice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Page<Product> products = productService.getProductsSortedByPrice(page, size, sortDirection);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/sort-by-popularity")
    public ResponseEntity<Page<Product>> getProductsSortedByPopularity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> products = productService.getProductsSortedByPopularity(page, size);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/sort-by-price-and-category")
    public ResponseEntity<Page<Product>> getProductsByCategoryAndSortedByPrice(
            @RequestParam String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Page<Product> products = productService.getProductsByCategoryAndSortedByPrice(categoryId, page, size, sortDirection);

        return ResponseEntity.ok(products);
    }

    // Paginated search endpoint
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Product> products = productService.searchProducts(query, page, size);
        return ResponseEntity.ok(products);
    }



    @GetMapping("/product/{name}")
    public ResponseEntity<Product> getProduct(@PathVariable String name) {

        Optional<Product> productOpt = productRepo.findByTitleIgnoreCase(name);

        if ( productOpt.isEmpty() ){
            log.info("Product ({}) not found", name);
            return ResponseEntity.notFound().build();
        }

        Product product = productOpt.get();

        return ResponseEntity.ok(product);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        Optional<Product> productOpt = productRepo.findById(id);

        if (productOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Product with ID " + id + " not found.");
        }

        return ResponseEntity.ok(productOpt.get());
    }



//    Get paginated products:            /api/product/paginated?page=0&size=10
//    Get products sorted by price:      /api/product/sort-by-price?page=0&size=10
//    Get products sorted by popularity: /api/product/sort-by-popularity?page=0&size=10


}
