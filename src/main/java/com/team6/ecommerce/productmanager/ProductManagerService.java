package com.team6.ecommerce.productmanager;


import com.team6.ecommerce.category.Category;
import com.team6.ecommerce.category.CategoryRepository;
import com.team6.ecommerce.comment.Comment;
import com.team6.ecommerce.comment.CommentRepository;
import com.team6.ecommerce.product.Product;
import com.team6.ecommerce.product.ProductRepository;
import com.team6.ecommerce.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Log4j2
@AllArgsConstructor
@Service
public class ProductManagerService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final CommentRepository commentRepo;

    @Secured({"ROLE_PRODUCTMANAGER"})
    public Set<Category> listAllCategories(){
        return null;
    }

    @Secured({"ROLE_PRODUCTMANAGER"})
    public void removeAllCategories(){
        categoryRepo.deleteAll();
    }

    @Secured({"ROLE_PRODUCTMANAGER"})
    public void addCategory(Category category){
        //productManagerService.addCategory(category);

    }

    @Transactional
    @Secured({"ROLE_PRODUCTMANAGER"})
    public void removeCategory(String categoryId){
        if (categoryRepo.existsById(categoryId)){
            categoryRepo.deleteById(categoryId);
            //TODO
        } else {
            //CategoryNotFoundException yaz
            throw new RuntimeException("Category not found");
        }
    }


//━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    @Secured({"ROLE_PRODUCTMANAGER"})
    public List<?> listInvoices (){
        return null;
    }


    @Secured({"ROLE_PRODUCTMANAGER"})
    public List<?> listInvoiceByCustomer( String customerId){
        return null;
    }



//━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    @Secured({"ROLE_PRODUCTMANAGER"})
    public List<Comment> listUnapprovedComments() {
        return commentRepo.findByApprovedFalse();
    }

    @Secured({"ROLE_PRODUCTMANAGER"})
    public List<Comment> listAllCommentsForProduct(String productId) {
        return commentRepo.findByProductId(productId);
    }

//━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//


//    @Secured({"ROLE_PRODUCTMANAGER"})
//    public void addProduct(ProductDTO dto){
//
//    }
//
//    @Secured({"ROLE_PRODUCTMANAGER"})
//    public void removeProduct(String productId){
//
//    }

    //old
    @Transactional
    @Secured({"ROLE_PRODUCTMANAGER"})
    public Product updateProductStock(String productId, int quantity) {

        //TODO ProductNotFoundException kullan
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setQuantityInStock(quantity);

        return productRepo.save(product);
    }






}

