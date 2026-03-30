package com.techouts.order_service.feignclient;

import com.techouts.order_service.dto.CartResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CartClientFallback implements CartClient {
    @Override
    public CartResponseDTO serveCartItems(Integer userId) {
        return new CartResponseDTO ("Cart endpoint cannot be reached at this time");
    }

    @Override
    public ResponseEntity<String> addProductToCart(int productId, int quantity, Integer userId) {
        return ResponseEntity.status (HttpStatus.REQUEST_TIMEOUT).body ("Cart endpoint cannot be reached at this time");
    }

    @Override
    public ResponseEntity<String> removeProductFromCart(int cartItemId, Integer userId) {
        return ResponseEntity.status (HttpStatus.REQUEST_TIMEOUT).body ("Cart endpoint cannot be reached at this time");
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateCartItem(int cartItemId, int quantity, Integer userId) {

        Map<String, Object> response = new HashMap<> ();
        response.put ("message", "Cart endpoint cannot be reached at this time");

        return ResponseEntity.status (HttpStatus.REQUEST_TIMEOUT).body (response);

    }

    @Override
    public ResponseEntity<CartResponseDTO> emptyUserCart(Integer userId) {
        return ResponseEntity.status (HttpStatus.REQUEST_TIMEOUT).body (new CartResponseDTO ("Cart endpoint cannot be reached at this time"));
    }
}
