package com.techouts.order_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemDTO {

    private int productId;

    private int orderId;

    private int quantity;

    private float purchasedPrice;

}
