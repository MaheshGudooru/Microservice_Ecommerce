package com.techouts.cart_service.repository;

import com.techouts.cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUserId(int userId);

}
