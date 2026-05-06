package com.techouts.cart_service.feignclient;


import com.techouts.cart_service.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallback implements ProductClient{
    @Override
    public ProductDTO getProductById(int id) {
        return new ProductDTO ("Products endpoint be reached at this time");
    }

}
