package com.techouts.cart_service.dto;

import com.techouts.order_service.dto.CartItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class CartResponseDTO {

    private String message;
    private List<CartItemDTO> items;

    public CartResponseDTO(List<CartItemDTO> items) {
        this.items = items;
    }

    public CartResponseDTO(String message) {
        this.message = message;
    }

}
