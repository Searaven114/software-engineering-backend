package com.team6.ecommerce.product;

import com.team6.ecommerce.exception.ProductNotFoundException;
import com.team6.ecommerce.notification.NotificationService;
import com.team6.ecommerce.product.dto.ProductDTO;
import com.team6.ecommerce.util.ImageLoader;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final NotificationService notificationService;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }



    // Fetch paginated products without sorting
    public Page<Product> getPaginatedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.findAll(pageable);
    }

    // Fetch products sorted by price dynamically by asc/desc
    public Page<Product> getProductsSortedByPrice(int page, int size, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by("basePrice").descending()
                : Sort.by("basePrice").ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepo.findAll(pageable);
    }

    // Fetch products sorted by popularity (descending)
    public Page<Product> getProductsSortedByPopularity(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("popularityPoint").descending());
        return productRepo.findAllByOrderByPopularityPointDesc(pageable);
    }

    public Product getProductById(String id) throws Exception {

        Optional<Product> product = productRepo.findById(id);
        return product.orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Fetch products filtered by category and sorted by price
    public Page<Product> getProductsByCategoryAndSortedByPrice(String categoryId, int page, int size, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by("basePrice").descending()
                : Sort.by("basePrice").ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        // Find by category and apply pagination with sorting
        return productRepo.findByCategoryId(categoryId, pageable);
    }

    // Method to handle PAGINATED search
    public Page<Product> searchProducts(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, pageable);
    }


//━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    @Transactional
    @Secured({"ROLE_SALESMANAGER", "ROLE_ADMIN"})
    public Product updateProductPrice(String id, double newPrice) {

        if (newPrice <= 0){
            throw new IllegalArgumentException("Product price cannot be lower or equal than 0");
        }

        Product product = productRepo.findById(id).orElse(null);

        if (product != null) {

            product.setBasePrice(newPrice);

            productRepo.save(product);

            //bunu siktir et, sadece discount notifi var
            //notificationService.notifyUsersAboutPriceChange(product);

            return product;
        }
          throw new ProductNotFoundException("Product with it id " + id + " is not found");
    }


    @Transactional
    @Secured({"ROLE_SALESMANAGER", "ROLE_ADMIN"})
    public Product applyDiscount(String id, double discountRate) {

        Optional<Product> productOptional = productRepo.findById(id);

        if ( productOptional.isPresent() ) {

            Product product = productOptional.get();

            double newPrice = product.getBasePrice() * (1 - discountRate / 100);

            product.setBasePrice( newPrice );

            productRepo.save(product);

            notificationService.notifyUsersAboutDiscount(id,discountRate);

            return product;
        }
        throw new ProductNotFoundException("Product not found");
    }


    //━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    private final Map<String, byte[]> images = ImageLoader.loadProductImages();

    @Transactional
    @Secured({"ROLE_ADMIN", "ROLE_PRODUCTMANAGER"})
    public Product addProduct(ProductDTO productDTO) {
        if (productRepo.existsByTitle(productDTO.getTitle())) {
            throw new IllegalArgumentException("A product with this title already exists");
        }

        if (productRepo.existsBySerialNumber(productDTO.getSerialNumber())) {
            throw new IllegalArgumentException("A product with this serial number already exists");
        }

        Product product = new Product(
                productDTO.getTitle(),
                productDTO.getCategoryId(),
                productDTO.getBrand(),
                productDTO.getModel(),
                productDTO.getSerialNumber(),
                productDTO.getDescription(),
                productDTO.getQuantityInStock(),
                productDTO.getBasePrice(),
                productDTO.isWarrantyStatus(),
                productDTO.getDistributorId(),
                images.get("666666")
        );

        return productRepo.save(product);
    }



    @Transactional
    @Secured({"ROLE_ADMIN", "ROLE_PRODUCTMANAGER"})
    public void removeProduct(String productId) {
        Optional<Product> productOptional = productRepo.findById(productId);

        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("Product with ID " + productId + " not found.");
        }

        Product product = productOptional.get();

        // Additional logic if needed, like logging, cleaning up associated data, etc.
        productRepo.delete(product);
        log.info("[ProductService][removeProduct] Product with ID: {} removed successfully.", productId);
    }


























}
