package com.techouts.order_service.controller;

import com.techouts.order_service.dto.OrderDTO;
import com.techouts.order_service.dto.PlaceOrderRequest;
import com.techouts.order_service.dto.StatusChangeRequest;
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
@RequestMapping("/orders")
public class OrderController {

    OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Object> getOrders(@RequestHeader("X-User-Id") Integer userId) {

        List<OrderDTO> userOrders = orderService.getOrderByUser(userId);

        if(userOrders == null || userOrders.isEmpty ()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User does not have any orders yet");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(userOrders);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody PlaceOrderRequest request,
                               @RequestHeader("X-User-Id") Integer userId) {

        OrderDTO orderDTO =  orderService.placeOrder (userId, request.getAddress(), request.getPaymentMethod());

        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> changeOrderDeliveryStatus(
            @PathVariable int orderId,
            @RequestBody StatusChangeRequest request,
            @RequestHeader("X-User-Id") Integer userId) {

        Order order = orderService.getOrderById (orderId);

        if(order.getUserId () != userId) {
            return ResponseEntity.status (HttpStatus.UNAUTHORIZED).body (new OrderDTO ("This order belongs to another user"));
        }

        OrderDTO changedOrderDTO = orderService.changeOrderStatus (orderId, request.getDeliveryStatus());

        return ResponseEntity.ok (changedOrderDTO);
    }

}

