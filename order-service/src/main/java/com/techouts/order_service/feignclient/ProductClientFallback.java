package com.techouts.order_service.feignclient;

import com.techouts.order_service.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProductClientFallback implements ProductClient{
    @Override
    public ProductDTO getProductById(int id) {
        return new ProductDTO ("Products endpoint be reached at this time");
    }

    @Override
    public Map<String, Object> getProducts(Integer pageNo) {

        Map<String, Object> response = new HashMap<> ();
        response.put ("message", "Products endpoint be reached at this time");
        return response;

    }

    @Override
    public ProductDTO updateProductStock(int productId, int newStock) {
        return new ProductDTO ("Products endpoint be reached at this time");
    }
}
