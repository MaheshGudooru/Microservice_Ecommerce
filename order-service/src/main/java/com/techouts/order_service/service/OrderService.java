package com.techouts.order_service.service;


import com.techouts.order_service.dto.*;
import com.techouts.order_service.feignclient.CartClient;
import com.techouts.order_service.feignclient.ProductClient;
import com.techouts.order_service.model.Order;
import com.techouts.order_service.model.OrderItem;
import com.techouts.order_service.repository.OrderItemRepo;
import com.techouts.order_service.repository.OrderRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

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
    public OrderDTO placeOrder(int userId, String address, String paymentMethod) {

        CartResponseDTO response = cartClient.serveCartItems(userId);

        if(response.getItems() == null) {
             return new OrderDTO ("Cannot place order since cart is empty");
        }

        List<CartItemDTO> cartItemDTOList = response.getItems();

        float totalCalculatedPrice = 0;

        for (CartItemDTO cartItem : cartItemDTOList) {

            ProductDTO currProduct = productClient.getProductById(cartItem.getProductId());

            if(currProduct.getStock () < cartItem.getQuantity()) {

                if(currProduct.getStock () == 0) {
                    return new OrderDTO (currProduct.getName () + " is out of stock");
                }
                return new OrderDTO ("Stock available for " + currProduct.getName () + " is " + currProduct.getStock ());
            }

            totalCalculatedPrice += cartItem.getQuantity() * currProduct.getPrice();

        }

//        if(totalPrice != totalCalculatedPrice) {
//            return new OrderDTO ("total price of the cart is incorrect");
//        }

        Order order = new Order(userId, totalCalculatedPrice, LocalDate.now().plusDays(3), paymentMethod, address);

        Order savedOrder = orderRepoImpl.save (order);

        List<CartItemDTO> cartItemsToBeRemoved = cartClient.serveCartItems (userId).getItems ();

        if(cartItemsToBeRemoved == null || cartItemsToBeRemoved.isEmpty ()) {
            return new OrderDTO ("User cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<> ();

        List<OrderItemDTO> orderItemDTOList = new ArrayList<> ();

        for (CartItemDTO cartItem : cartItemsToBeRemoved) {

            int currProduct = cartItem.getProductId();

            ProductDTO currProductObj = productClient.getProductById (currProduct);

            int updatedStock = currProductObj.getStock () - cartItem.getQuantity ();

            if (updatedStock < 0) {
                return new OrderDTO ("Stock calculation error!");
            }

            productClient.updateProductStock (currProduct, updatedStock);

            orderItems.add(
                    new OrderItem(
                        cartItem.getProductId(),
                        order,
                        cartItem.getQuantity(),
                        currProductObj.getPrice ()
                    )
            );

            orderItemDTOList.add (
                    new OrderItemDTO (
                            cartItem.getProductId (),
                            order.getId (),
                            cartItem.getQuantity (),
                            currProductObj.getPrice ()
                    )
            );

        }

        ResponseEntity<CartResponseDTO> cartResponseDTO = cartClient.emptyUserCart (userId);

        if(cartResponseDTO.getStatusCode ().is5xxServerError ()) {
            return new OrderDTO ("something went wrong while emptying the cart");
        }

        savedOrder.setOrderItems(orderItems);

        orderRepoImpl.save(savedOrder);

        return new OrderDTO (orderItemDTOList);

    }

    @Transactional
    public List<OrderDTO> getOrderByUser(int userId) {

        List<Order> ordersList = orderRepoImpl.findAllByUserId(userId);

        if(ordersList == null || ordersList.isEmpty()) {
            return Collections.emptyList ();
        }

        List<OrderDTO> orderDTOList = new ArrayList<> ();

        for (Order order : ordersList) {

            List<OrderItem> allOrderItemsOfUser = orderItemRepoImpl.findAllByOrderId(order);

            List<OrderItemDTO> allOrderItemDTOList = new ArrayList<> ();

            for(OrderItem orderItem : allOrderItemsOfUser) {

                allOrderItemDTOList.add (new OrderItemDTO (orderItem.getProductId (), orderItem.getOrderId ().getId (), orderItem.getQuantity (), orderItem.getPurchasedPrice ()));

            }

            orderDTOList.add (new OrderDTO (
                    "Successfully fetched user orders",
                    allOrderItemDTOList,
                    order.getDeliveryStatus (),
                    order.getFormattedOrderedDate (),
                    order.getAddress (),
                    order.getPaymentType ()));

        }

//        return allOrderItemsOfUser.stream()
//                .collect(Collectors.groupingBy(
//                        item -> item.getOrderId().getId(),
//                        Collectors.mapping (
//                                item -> new OrderItemDTO (item.getProductId (), item.getOrderId ().getId (), item.getQuantity (), item.getPurchasedPrice ()),
//                                Collectors.toList ()
//                        )));

        return orderDTOList;

    }

    @Transactional
    public OrderDTO changeOrderStatus(int orderId, String deliveryStatus) {

        Order currOrder = orderRepoImpl.findById (orderId).orElse (null);

        if(currOrder == null) {
            return new OrderDTO ("Order does not exists or wrong order ID");
        }

        Set<String> possibleDeliveryStatus = new HashSet<> ();
        possibleDeliveryStatus.add ("PROCESSING");
        possibleDeliveryStatus.add ("SHIPPED");
        possibleDeliveryStatus.add ("IN_TRANSIT");
        possibleDeliveryStatus.add ("OUT_FOR_DELIVERY");
        possibleDeliveryStatus.add ("DELIVERED");
        possibleDeliveryStatus.add ("CANCELLED");
        possibleDeliveryStatus.add ("REFUNDED");
        possibleDeliveryStatus.add ("RETURNED");

        if(!possibleDeliveryStatus.contains (deliveryStatus.toUpperCase ())) {
            return new OrderDTO ("Please provide a valid delivery status");
        }

        currOrder.setDeliveryStatus (deliveryStatus.toUpperCase ());
        return new OrderDTO ("Successfully changed the order status to " + deliveryStatus.toUpperCase ());

    }

    public Order getOrderById(int orderId) {

        return orderRepoImpl.findById (orderId).orElse (null);

    }

}

