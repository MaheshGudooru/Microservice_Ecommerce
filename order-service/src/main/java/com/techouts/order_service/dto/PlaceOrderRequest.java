package com.techouts.order_service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlaceOrderRequest {
    private String address;
    private String paymentMethod;
}
