package com.techouts.cart_service.controller;


import com.techouts.cart_service.dto.CartResponseDTO;
import com.techouts.cart_service.model.Cart;
import com.techouts.cart_service.model.CartItem;
import com.techouts.cart_service.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<Map<String, Object>> serveCartItems(@RequestHeader("X-User-Id") Integer userId) {

        List<CartItem> userCartItems = cartService.getCartItemsByUser (userId);

        Map<String, Object> response = new HashMap<> ();

        if(userCartItems.isEmpty ()) {
            response.put ("message", "User cart is empty");
            return ResponseEntity.status (HttpStatus.NO_CONTENT).body (response);
        }

        response.put("cart items", userCartItems);
        return ResponseEntity.ok (response);

    }
//    @GetMapping
//    public ResponseEntity<Integer> serveCartItems(@RequestHeader("X-User-Id") Integer userId) {
//
//        return ResponseEntity.ok (userId);
//
//    }

    @PostMapping("add")
    public ResponseEntity<String> addProductToCart(@RequestParam("productId") int productId,
                                                   @RequestParam(name = "quantity", defaultValue = "1", required = false) int quantity,
                                                   @RequestHeader("X-User-Id") Integer userId) {

        boolean productAddedToCartStatus = cartService.addToCart(userId, productId, quantity);

        if (!productAddedToCartStatus) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide valid data");
        }

        return ResponseEntity.ok("success");

    }

    @PostMapping("create")
    public ResponseEntity<CartResponseDTO> createCartForUser(@RequestParam("userId") int userId){

        Cart cart = cartService.createUserCart (userId);

        CartResponseDTO responseDTO = new CartResponseDTO (cart.getId (), cart.getUserId ());

        return ResponseEntity.status (HttpStatus.CREATED).body (responseDTO);

    }

    @PostMapping("remove")
    public ResponseEntity<String> removeProductFromCart(@RequestParam("cartItemId") int cartItemId, @RequestHeader("X-User-Id") Integer userId) {

        cartService.removeCartItemFromCart(cartItemId, userId);

        return ResponseEntity.ok("success");

    }

    @PostMapping("update")
    public ResponseEntity<Map<String, Object>> updateCartItem(@RequestParam("cartItemId") int cartItemId,
                                                              @RequestParam("quantity") int quantity,
                                                              @RequestHeader("X-User-Id") Integer userId) {

        String message = cartService.updateCartItemQuantity (userId, cartItemId, quantity);
        Map<String, Object> response = new HashMap<> ();

        if(message.contains ("unauthorized")) {
            response.put ("message", "You are not authorized to access this cart");
            return ResponseEntity.status (HttpStatus.BAD_REQUEST).body (response);
        }

        response.put ("message", message);
        return  ResponseEntity.ok (response);

    }

}

