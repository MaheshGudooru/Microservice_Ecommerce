package com.techouts.product_service.controller;


import com.techouts.product_service.model.Product;
import com.techouts.product_service.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    ProductController(ProductService productService) {

        this.productService = productService;

    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") int id) {

        Product product = productService.getProduct(id);

        return product;

    }

    @GetMapping
    public Map<String, Object> getProducts(@RequestParam(name = "page", required = false) Integer pageNo) {

        Map<String, Object> response = new HashMap<> ();

        if(pageNo == null) {

            response.put ("products", productService.getProducts (null));
            return response;
        }


        int totalProductsCnt = productService.getProducts(null).size();


        response.put("products", productService.getProducts(pageNo));
        response.put("totalPages", (int) Math.ceil((double) totalProductsCnt / 12));
        response.put("pageNo", pageNo);

        return response;

    }

}

