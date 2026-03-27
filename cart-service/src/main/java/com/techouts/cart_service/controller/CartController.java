package com.techouts.cart_service.controller;


import com.techouts.cart_service.model.CartItem;
import com.techouts.cart_service.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    CartController(CartService cartService) {
        this.cartService = cartService;
    }

//    @GetMapping
//    public List<CartItem> serveCartItems(@RequestHeader("X-User-Id") String userId) {
//
//        return cartService.getCartItemsByUser(Integer.parseInt (userId));
//
//    }
    @GetMapping
    public ResponseEntity<String> serveCartItems() {

        return ResponseEntity.ok ("success");

    }

    @PostMapping("add")
    public ResponseEntity<String> addProductToCart(@RequestParam("productId") int productId,
                                                   @RequestParam(name = "quantity", defaultValue = "1", required = false) int quantity,
                                                   @RequestHeader("X-User-Id") String userId) {

        int actualUserId = Integer.parseInt (userId);

        boolean productAddedToCartStatus = cartService.addToCart(actualUserId, productId, quantity);

        if (!productAddedToCartStatus) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide valid data");
        }

        return ResponseEntity.ok("success");

    }

    @PostMapping("remove")
    public ResponseEntity<String> removeProductFromCart(@RequestParam("cartItemId") int cartItemId, @RequestHeader("X-User-Id") String userId) {

        cartService.removeCartItemFromCart(cartItemId, Integer.parseInt (userId));

        return ResponseEntity.ok("success");

    }

    @PostMapping("update")
    public ResponseEntity<Map<String, Object>> updateCartItem(@RequestParam("cartItemId") int cartItemId,
                                                              @RequestParam("quantity") int quantity,
                                                              @RequestHeader("X-User-Id") String userId) {

        String message = cartService.updateCartItemQuantity (Integer.parseInt (userId), cartItemId, quantity);
        Map<String, Object> response = new HashMap<> ();

        if(message.contains ("unauthorized")) {
            response.put ("message", "You are not authorized to access this cart");
            return ResponseEntity.status (HttpStatus.BAD_REQUEST).body (response);
        }

        response.put ("message", message);
        return  ResponseEntity.ok (response);

    }

}

