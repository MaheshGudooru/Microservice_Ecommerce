package com.techouts.order_service.feignclient;

import com.techouts.order_service.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallback implements ProductClient{
    @Override
    public ProductDTO getProductById(int id) {
        return new ProductDTO ("Products endpoint be reached at this time");
    }

    @Override
    public ProductDTO updateProductStock(int productId, int newStock) {
        return new ProductDTO ("Products endpoint be reached at this time");
    }
}
