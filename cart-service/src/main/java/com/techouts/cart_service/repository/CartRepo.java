package com.techouts.cart_service.repository;

import com.techouts.cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Integer> {

    Cart findByUserId(int userId);

}
