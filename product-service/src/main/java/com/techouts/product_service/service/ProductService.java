package com.techouts.product_service.service;


import com.techouts.product_service.model.Product;
import com.techouts.product_service.repository.ProductRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    ProductRepo productRepoImpl;

    ProductService(ProductRepo productRepoImpl) {

        this.productRepoImpl = productRepoImpl;

    }

    @Transactional
    public Product getProduct(int productId) {

        return productRepoImpl.findById (productId).orElse (new Product ());

    }

    @Transactional(readOnly = true)
    public List<Product> getProducts(String category) {

        List<Product> productsList = category == null || category.isBlank () ? productRepoImpl.findAll ()
                : productRepoImpl.findByCategory (category);

        return productsList == null ? new ArrayList<> () : productsList;

    }

    @Transactional(readOnly = true)
    public List<Product> getProducts(int page) {

        int productCnt = 12;

        return productRepoImpl.findAll (PageRequest.of (page - 1, productCnt)).stream ().toList ();

    }

    @Transactional
    public void saveProduct(Product product) {

        productRepoImpl.save (product);

    }


}

