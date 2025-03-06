package com.team6.ecommerce.salesmanager;


import com.team6.ecommerce.category.CategoryRepository;
import com.team6.ecommerce.distributor.DistributorRepository;
import com.team6.ecommerce.product.ProductRepository;
import com.team6.ecommerce.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@AllArgsConstructor
@Service
public class SalesManagerService {

    private final ProductService productService;
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final DistributorRepository distributorRepo;


}
