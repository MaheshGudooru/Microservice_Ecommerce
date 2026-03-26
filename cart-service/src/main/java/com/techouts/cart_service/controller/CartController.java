package com.techouts.cart_service.controller;


import com.techouts.cart_service.model.CartItem;
import com.techouts.cart_service.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<CartItem> serveCartItems(int userId) {

        return cartService.getCartItemsByUser(userId);

    }

    @PostMapping("/add")
    public ResponseEntity<String> addProductToCart(@RequestParam("productId") int productId,
                                                   @RequestParam(name = "quantity", defaultValue = "1", required = false) int quantity,
                                                   Integer userId) {

        if (userId == null) {
            return ResponseEntity.status(401).body("login_required");
        }
        boolean productAddedToCartStatus = cartService.addToCart(userId, productId, quantity);

        if (!productAddedToCartStatus) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide valid data");
        }

        return ResponseEntity.ok("success");

    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeProductFromCart(@RequestParam("cartItemId") int cartItemId) {

        cartService.removeCartItemFromCart(cartItemId);

        return ResponseEntity.ok("success");

    }

    @PostMapping("/increasecnt")
    public ResponseEntity<String> increaseProductQuantity(@RequestParam("cartItemId") int cartItemId) {

        cartService.changeCartItemQuantity(cartItemId, true);

        return ResponseEntity.ok("success");

    }

    @PostMapping("/decreasecnt")
    public ResponseEntity<String> decreaseProductQuantity(@RequestParam("cartItemId") int cartItemId) {

        cartService.changeCartItemQuantity(cartItemId, false);

        return ResponseEntity.ok("success");

    }
}

