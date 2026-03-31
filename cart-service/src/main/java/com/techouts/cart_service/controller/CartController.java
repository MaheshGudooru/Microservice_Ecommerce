package com.techouts.cart_service.controller;


import com.techouts.cart_service.dto.CartItemDTO;
import com.techouts.cart_service.dto.CartResponseDTO;
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
    public ResponseEntity<CartResponseDTO> serveCartItems(@RequestHeader("X-User-Id") Integer userId) {

        List<CartItemDTO> userCartItems = cartService.getCartItemsByUser (userId);


        if(userCartItems.isEmpty ()) {
            return ResponseEntity.ok(new CartResponseDTO("User cart is empty"));
        }

        return ResponseEntity.ok (new CartResponseDTO(userCartItems));

    }

    @PostMapping("add")
    public ResponseEntity<Map<String, String>> addProductToCart(@RequestParam("productId") int productId,
                                                   @RequestParam(name = "quantity", defaultValue = "1", required = false) int quantity,
                                                   @RequestHeader("X-User-Id") Integer userId) {

        boolean productAddedToCartStatus = cartService.addToCart(userId, productId, quantity);

        Map<String, String> response = new HashMap<> ();

        if (!productAddedToCartStatus) {

            response.put("message", "Product does not exist or the product is currently out of stock");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put ("message", "Successfully added the product to cart");

        return ResponseEntity.ok(response);

    }

    @PostMapping("remove")
    public ResponseEntity<Map<String, String>> removeProductFromCart(@RequestParam("cartItemId") int cartItemId,
                                                                     @RequestHeader("X-User-Id") Integer userId) {

        String removalStatus = cartService.removeCartItemFromCart(cartItemId, userId);

        Map<String, String> response = new HashMap<> ();

        if(removalStatus.equals ("unauthorized")) {
            response.put ("message", "Unauthorized to access other user details");
            return ResponseEntity.status (HttpStatus.UNAUTHORIZED).body (response);
        }

        response.put ("message", "Successfully removed product from cart");

        return ResponseEntity.ok(response);

    }

    @PostMapping("empty")
    public ResponseEntity<CartResponseDTO> emptyUserCart(@RequestHeader("X-User-Id") Integer userId) {

        String cartItemsRemovalStatus = cartService.RemoveAllCartItemsFromCart (userId);

        return ResponseEntity.ok (new CartResponseDTO (cartItemsRemovalStatus));

    }

    @PostMapping("update")
    public ResponseEntity<Map<String, Object>> updateCartItem(@RequestParam("cartItemId") int cartItemId,
                                                              @RequestParam("quantity") int quantity,
                                                              @RequestHeader("X-User-Id") Integer userId) {

        Map<String, Object> response = new HashMap<> ();
        if(quantity < 0) {
            response.put ("message", "Please provide a valid quantity");
            return ResponseEntity.status (HttpStatus.BAD_REQUEST).body (response);
        }

        String message = cartService.updateCartItemQuantity (userId, cartItemId, quantity);

        if(message.contains ("unauthorized")) {
            response.put ("message", "You are not authorized to access this cart");
            return ResponseEntity.status (HttpStatus.BAD_REQUEST).body (response);
        }

        response.put ("message", message);
        return  ResponseEntity.ok (response);

    }

}

