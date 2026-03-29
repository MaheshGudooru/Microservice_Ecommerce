package com.techouts.cart_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cartId;

    @CreationTimestamp
    @Column(name = "added_date")
    private LocalDateTime addedToCartDate;

    private int quantity;

    @Column(name = "product_id", nullable = false)
    private int productId;

    public CartItem(Cart cartId, int productId, int quantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
    }

}

