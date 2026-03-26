package com.techouts.product_service.repository;

import com.techouts.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<Product> findByNameAndCategoryAndPriceAndProductDescriptionAndProductImage(String name, String category, float price, String productDescription, String productImage);

    List<Product> findByCategory(String category);


}
