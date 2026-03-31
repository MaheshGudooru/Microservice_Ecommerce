package com.techouts.cart_service.feignclient;


import com.techouts.cart_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "API-GATEWAY",
        contextId = "productClient",
        fallback = ProductClientFallback.class)
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductDTO getProductById(@PathVariable("id") int id) ;

    @GetMapping("/api/products")
    Map<String, Object> getProducts(@RequestParam(name = "page", required = false) Integer pageNo) ;

    @PostMapping("/api/products/update")
    ProductDTO updateProductStock(@RequestParam(name = "productId") int productId, @RequestParam(name = "newStock") int newStock) ;

}
