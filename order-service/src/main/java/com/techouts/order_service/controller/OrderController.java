package com.techouts.order_service.controller;

import com.techouts.order_service.model.OrderItem;
import com.techouts.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Object> serveOrdersPage(@RequestHeader("X-User-Id") Integer userId) {

        Map<Integer, List<OrderItem>> userOrders = orderService.getOrderByUser(userId);

        if(userOrders == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User does not have any orders yet");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(userOrders);
    }

    @PostMapping
    public String placeOrder(@Valid @RequestParam("address") String address,
                             @Valid @RequestParam("paymentMethod") String paymentMethod,
                             @RequestParam("cartTotalPrice") float cartTotalPrice,
                             @RequestHeader("X-User-Id") Integer userId) {

        String message = orderService.placeOrder (userId, address, cartTotalPrice, paymentMethod);

        if(!"success".equals (message)) {
            redirectAttributes.addFlashAttribute ("message", message);
            return "redirect:/cart";
        }

        return "redirect:/order";

    }
//
//    @PostMapping("cancel")
//    public ResponseEntity<String> cancelOrder(@RequestParam("orderId") int orderId) {
//
//        orderService.cancelOrder (orderId);
//
//        return ResponseEntity.ok ("success");
//    }

}

