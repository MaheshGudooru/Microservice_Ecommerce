package com.techouts.cart_service.repository;

import com.techouts.cart_service.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

}
