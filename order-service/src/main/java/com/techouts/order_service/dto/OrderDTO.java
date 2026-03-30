package com.techouts.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private String message;

    private List<OrderItemDTO> orderItemDTOList;

    private String deliveryStatus;

    private String orderedDate;

    private String address;

    private String paymentType;

    public OrderDTO(List<OrderItemDTO> orderItemDTOList) {
        this.orderItemDTOList = orderItemDTOList;
    }

    public OrderDTO(String message) {
        this.message = message;
    }

}
