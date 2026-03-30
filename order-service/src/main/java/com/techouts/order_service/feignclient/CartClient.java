package com.techouts.order_service.feignclient;

import com.techouts.order_service.config.FeignConfig;
import com.techouts.order_service.dto.CartResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@FeignClient(name = "API-GATEWAY",
        contextId = "cartClient",
        configuration = FeignConfig.class,
        fallback = CartClientFallback.class)
public interface CartClient {

    @GetMapping("/api/cart")
    CartResponseDTO serveCartItems(@RequestHeader("X-User-Id") Integer userId);

    @PostMapping("/api/cart/add")
    ResponseEntity<String> addProductToCart(@RequestParam("productId") int productId,
                                                   @RequestParam(name = "quantity", defaultValue = "1", required = false) int quantity,
                                                   @RequestHeader("X-User-Id") Integer userId);


    @PostMapping("/api/cart/remove")
    ResponseEntity<String> removeProductFromCart(@RequestParam("cartItemId") int cartItemId, @RequestHeader("X-User-Id") Integer userId);

    @PostMapping("/api/cart/update")
    ResponseEntity<Map<String, Object>> updateCartItem(@RequestParam("cartItemId") int cartItemId,
                                                              @RequestParam("quantity") int quantity,
                                                              @RequestHeader("X-User-Id") Integer userId);
    @PostMapping("/api/cart/empty")
    public ResponseEntity<CartResponseDTO> emptyUserCart(@RequestHeader("X-User-Id") Integer userId);
}
