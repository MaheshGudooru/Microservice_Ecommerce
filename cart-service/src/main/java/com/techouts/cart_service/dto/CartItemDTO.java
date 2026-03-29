package com.techouts.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CartItemDTO {

    private int id;
    private int productId;
    private int quantity;

}
