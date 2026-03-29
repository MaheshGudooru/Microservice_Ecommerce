package com.techouts.order_service.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "delivery_status")
    private String deliveryStatus = "PROCESSING";

    @Column(name = "total_price")
    private float totalPrice;

    @Column(name = "delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "ordered_date")
    private LocalDate orderedDate;

    @NotBlank(message = "please select a payment type")
    @Column(name = "payment_type")
    private String paymentType;

    @NotBlank(message = "enter delivery address")
    @Column(name = "delivery_address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private List<OrderItem> orderItems;

    public Order(int userId, float totalPrice, LocalDate estimatedDeliveryDate, String paymentType, String address) {

        this.userId = userId;
        this.totalPrice = totalPrice;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.paymentType = paymentType;
        this.address = address;
        this.orderedDate = LocalDate.now();
        this.estimatedDeliveryDate = LocalDate.now().plusDays(3);

    }

    public String getFormattedOrderedDate() {
        if (orderedDate == null)
            return "";
        return orderedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
    }

}

