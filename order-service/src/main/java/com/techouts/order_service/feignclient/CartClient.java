package com.techouts.order_service.feignclient;

import com.techouts.cart_service.dto.CartResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@FeignClient(name = "API-GATEWAY")
@RequestMapping("/cart")
public interface CartClient {

    @GetMapping
    CartResponseDTO serveCartItems(@RequestHeader("X-User-Id") Integer userId);

    @PostMapping("add")
    ResponseEntity<String> addProductToCart(@RequestParam("productId") int productId,
                                                   @RequestParam(name = "quantity", defaultValue = "1", required = false) int quantity,
                                                   @RequestHeader("X-User-Id") Integer userId);


    @PostMapping("remove")
    ResponseEntity<String> removeProductFromCart(@RequestParam("cartItemId") int cartItemId, @RequestHeader("X-User-Id") Integer userId);

    @PostMapping("update")
    ResponseEntity<Map<String, Object>> updateCartItem(@RequestParam("cartItemId") int cartItemId,
                                                              @RequestParam("quantity") int quantity,
                                                              @RequestHeader("X-User-Id") Integer userId);
}
