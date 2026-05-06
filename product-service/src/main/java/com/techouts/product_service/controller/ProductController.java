package com.techouts.product_service.controller;


import com.techouts.product_service.dto.ProductDTO;
import com.techouts.product_service.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {

        this.productService = productService;

    }

    @GetMapping("{id}")
    public ProductDTO getProductById(@PathVariable int id) {

        return productService.getProduct(id);

    }

    @GetMapping
    public Map<String, Object> getProducts(@RequestParam(name = "page", required = false) Integer pageNo, @RequestParam(required = false) String category) {

        Map<String, Object> response = new HashMap<> ();

        if(pageNo == null) {

            response.put ("products", productService.getProducts (category));
            return response;
        }


        int totalProductsCnt = productService.getProducts(category).size();

        int pageIdx = Math.max (0, pageNo);

        response.put("products", productService.getProducts(pageIdx, category));
        response.put("totalPages", (int) Math.ceil((double) totalProductsCnt / 12));
        response.put("pageNo", pageIdx);

        return response;

    }

    @PostMapping("update")
    public ProductDTO updateProductStock(@RequestParam int productId, @RequestParam int newStock) {

        return productService.updateProductStock (productId, newStock);

    }


}

