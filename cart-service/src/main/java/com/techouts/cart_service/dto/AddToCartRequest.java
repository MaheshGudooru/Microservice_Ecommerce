package com.techouts.cart_service.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
    private int productId;
    private int quantity = 1;
}
