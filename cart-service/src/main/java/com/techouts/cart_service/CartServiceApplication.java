package com.techouts.cart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CartServiceApplication {

	static {
		System.setProperty("user.timezone", "Asia/Kolkata");
	}

	public static void main(String[] args) {
		SpringApplication.run(CartServiceApplication.class, args);
	}

}
