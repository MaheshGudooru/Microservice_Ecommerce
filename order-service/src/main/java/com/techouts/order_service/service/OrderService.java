package com.techouts.order_service.service;


import com.techouts.cart_service.dto.CartResponseDTO;
import com.techouts.order_service.dto.CartItemDTO;
import com.techouts.order_service.dto.ProductDTO;
import com.techouts.order_service.feignclient.CartClient;
import com.techouts.order_service.feignclient.ProductClient;
import com.techouts.order_service.model.Order;
import com.techouts.order_service.model.OrderItem;
import com.techouts.order_service.repository.OrderItemRepo;
import com.techouts.order_service.repository.OrderRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepo orderRepoImpl;
    private final OrderItemRepo orderItemRepoImpl;
    private final CartClient cartClient;
    private final ProductClient productClient;


    OrderService(OrderRepo orderRepoImpl, OrderItemRepo orderItemRepoImpl, CartClient cartClient, ProductClient productClient) {

        this.orderRepoImpl = orderRepoImpl;
        this.orderItemRepoImpl = orderItemRepoImpl;
        this.cartClient = cartClient;
        this.productClient = productClient;

    }

    @Transactional
    public String placeOrder(int userId, String address, float totalPrice, String paymentMethod) {

        CartResponseDTO response = cartClient.serveCartItems(userId);

        if(response.getItems() == null) {
             return "Cannot place order since cart is empty";
        }

        List<CartItemDTO> cartItemDTOList = response.getItems();

        float totalCalculatedPrice = 0;

        for (CartItemDTO cartItem : cartItemDTOList) {

            ProductDTO currProduct = productClient.getProductById(cartItem.getProductId());

            if(currProduct.getStock () < cartItem.getQuantity()) {

                if(currProduct.getStock () == 0) {
                    return currProduct.getName () + " is out of stock";
                }
                return "Stock available for " + currProduct.getName () + " is " + currProduct.getStock ();
            }

            totalCalculatedPrice += cartItem.getQuantity() * currProduct.getPrice();

        }

        Order order = new Order(userId, totalCalculatedPrice, LocalDate.now().plusDays(3), paymentMethod, address);

        // CONTINUE FROM HERE CREATED ORDER ENTITY BUT DIDNOT PERSIST IT

        List<CartItem> removedCartItems = cartRepoImpl.clearCart(user.getCart());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : removedCartItems) {

            Product currProduct = cartItem.getProductId();

            int updatedStock = currProduct.getStock () - cartItem.getQuantity ();

            if (updatedStock < 0) {
                throw new RuntimeException("Stock calculation error!");
            }

            currProduct.setStock (updatedStock);

            orderItems.add(new OrderItem(
                    cartItem.getProductId(),
                    order,
                    cartItem.getQuantity(),
                    cartItem.getProductId().getPrice()));

        }

        order.setOrderItems(orderItems);

        orderRepoImpl.saveOrder(order);

        return "success";

    }

    @Transactional
    public Map<Integer, List<OrderItem>> getOrderByUser(int userId) {

        List<Order> ordersList = orderRepoImpl.findAllByUserId(userId);

        if(ordersList == null || ordersList.isEmpty()) {
            return null;
        }

        List<OrderItem> allOrderItemsOfUser = new ArrayList<>();

        for (Order order : ordersList) {

            allOrderItemsOfUser.addAll(orderItemRepoImpl.findAllByOrderId(order.getId()));

        }

        return allOrderItemsOfUser.stream()
                .collect(Collectors.groupingBy(item -> item.getOrderId().getId()));

    }

//    @Transactional
//    public void cancelOrder(int orderId) {
//
//        Order currOrder = orderRepoImpl.getById(orderId);
//
//        List<OrderItem> orderItems = currOrder.getOrderItems ();
//
//        for(OrderItem orderItem : orderItems) {
//
//            int cancelledOrderStock = orderItem.getQuantity ();
//            int alreadyPresentProductStock = orderItem.getProductId ().getStock ();
//
//            orderItem.getProductId ().setStock (cancelledOrderStock + alreadyPresentProductStock);
//
//            productRepoImpl.save (orderItem.getProductId ());
//
//            System.out.println("Product ID: " + orderItem.getProductId().getId());
//            System.out.println("Current Stock: " + alreadyPresentProductStock);
//            System.out.println("Cancelled Qty: " + cancelledOrderStock);
//
//        }
//
//        currOrder.setDeliveryStatus("CANCELLED");
//
//    }

}

