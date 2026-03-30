package com.techouts.order_service.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductDTO {

    private String message;

    private Integer productId;

    private String name;

    private Float price;

    private String productDesc;

    private String category;

    private String imageUrl;

    private Integer stock;

    public ProductDTO(String message) {
        this.message = message;
    }

    public ProductDTO(int productId, String name, float price, String productDesc, int stock, String category, String imageUrl) {

        this.productId = productId;
        this.name = name;
        this.price = price;
        this.productDesc = productDesc;
        this.stock = stock;
        this.category = category;
        this.imageUrl = imageUrl;

    }

}
