package com.techouts.order_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "product_id")
    private int productId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order orderId;

    private int quantity;

    @Column(name = "purchased_price")
    private float purchasedPrice;

    public OrderItem(int productId, Order orderId, int quantity, float purchasedPrice) {

        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.purchasedPrice = purchasedPrice;

    }
}
