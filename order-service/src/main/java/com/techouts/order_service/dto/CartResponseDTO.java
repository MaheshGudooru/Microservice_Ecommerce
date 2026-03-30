package com.techouts.order_service.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
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
