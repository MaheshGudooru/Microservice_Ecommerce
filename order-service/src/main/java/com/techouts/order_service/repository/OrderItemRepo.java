package com.techouts.order_service.repository;

import com.techouts.order_service.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findAllByOrderId(int id);

}
