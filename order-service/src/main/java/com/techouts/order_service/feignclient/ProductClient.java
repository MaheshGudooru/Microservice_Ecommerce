package com.techouts.order_service.feignclient;

import com.techouts.order_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "API-SERVICE")
@RequestMapping("/products")
public interface ProductClient {

    @GetMapping("/{id}")
    ProductDTO getProductById(@PathVariable("id") int id) ;

    @GetMapping
    Map<String, Object> getProducts(@RequestParam(name = "page", required = false) Integer pageNo) ;

}
