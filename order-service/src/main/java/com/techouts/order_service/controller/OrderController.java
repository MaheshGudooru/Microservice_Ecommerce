package com.techouts.order_service.controller;

import com.techouts.order_service.dto.OrderDTO;
import com.techouts.order_service.model.Order;
import com.techouts.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

        List<OrderDTO> userOrders = orderService.getOrderByUser(userId);

        if(userOrders == null || userOrders.isEmpty ()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User does not have any orders yet");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(userOrders);
    }

    @PostMapping
    public OrderDTO placeOrder(@Valid @RequestParam("address") String address,
                               @Valid @RequestParam("paymentMethod") String paymentMethod,
                               @RequestHeader("X-User-Id") Integer userId) {

        return orderService.placeOrder (userId, address, paymentMethod);

    }

    @PostMapping("orderstatus")
    public ResponseEntity<OrderDTO> changeOrderDeliveryStatus(
            @RequestParam("orderId") int orderId,
            @RequestParam("status") String deliveryStatus,
            @RequestHeader("X-User-Id") Integer userId) {

        Order order = orderService.getOrderById (orderId);

        if(order.getUserId () != userId) {
            return ResponseEntity.status (HttpStatus.UNAUTHORIZED).body (new OrderDTO ("This order belongs to another user"));
        }

        OrderDTO changedOrderDTO = orderService.changeOrderStatus (orderId, deliveryStatus);

        return ResponseEntity.ok (changedOrderDTO);
    }

}

