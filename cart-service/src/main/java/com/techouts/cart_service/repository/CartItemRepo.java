package com.techouts.cart_service.repository;

import com.techouts.cart_service.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

     Optional<CartItem> findByProductId(int productId);

}
