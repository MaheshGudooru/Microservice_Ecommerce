package com.techouts.order_service.feignclient;

import com.techouts.order_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "API-GATEWAY",
        contextId = "productClient",
        fallback = ProductClientFallback.class)
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductDTO getProductById(@PathVariable("id") int id) ;

    @PostMapping("/api/products/update")
    ProductDTO updateProductStock(@RequestParam(name = "productId") int productId, @RequestParam(name = "newStock") int newStock) ;

}
