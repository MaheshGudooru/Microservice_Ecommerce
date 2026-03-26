package com.techouts.cart_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
}
