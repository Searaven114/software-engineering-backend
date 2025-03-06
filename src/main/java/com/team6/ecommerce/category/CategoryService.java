package com.team6.ecommerce.category;

import com.team6.ecommerce.product.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    public List<Category> getAllActiveCategory() {
        List<Category> categories = categoryRepo.findByIsActiveTrue();
        return categories;
    }

    public List<Category> findAll() {
        return categoryRepo.findAll();

    }

    public List<Category> findAllActive(){
        return categoryRepo.findByIsActiveTrue();
    }

    public Optional<Category> findById(String id) {
        return categoryRepo.findById(id);

    }

    @Transactional
    public Category save(Category category) {
        return categoryRepo.save(category);

    }

    @Transactional
    public boolean deleteCategory(String categoryId) {
        // Delete all products associated with the category
        productRepo.findByCategoryId(categoryId)
                .forEach(product -> productRepo.delete(product));

        // Delete the category itself
        categoryRepo.deleteById(categoryId);
        return true;
    }


    public Category update(String id, Category categoryDetails) {
        return categoryRepo.findById(id)
                .map(category -> {
                    category.setName(categoryDetails.getName());
                    category.setIsActive(categoryDetails.getIsActive());

                    return categoryRepo.save(category);
                })
                .orElseGet(() -> {
                    categoryDetails.setId(id);
                    return categoryRepo.save(categoryDetails);
                });
    }

    @Transactional
    public Category createCategory(Category category) {

        if (categoryRepo.existsByName(category.getName())) {
            throw new IllegalArgumentException("A category with this name already exists.");
        }

        return categoryRepo.save(new Category(category.getName(), category.getIsActive()));
    }

}












