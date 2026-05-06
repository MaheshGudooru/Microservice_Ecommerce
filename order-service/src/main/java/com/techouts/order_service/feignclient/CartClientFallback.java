package com.techouts.order_service.feignclient;

import com.techouts.order_service.dto.CartResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CartClientFallback implements CartClient {
    @Override
    public CartResponseDTO serveCartItems(Integer userId) {
        return new CartResponseDTO ("Cart endpoint cannot be reached at this time");
    }

    @Override
    public ResponseEntity<CartResponseDTO> emptyUserCart(Integer userId) {
        return ResponseEntity.status (HttpStatus.REQUEST_TIMEOUT).body (new CartResponseDTO ("Cart endpoint cannot be reached at this time"));
    }
}
