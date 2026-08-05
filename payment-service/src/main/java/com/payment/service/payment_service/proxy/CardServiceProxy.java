package com.payment.service.payment_service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "CARD-SERVICE")
public interface CardServiceProxy {

    @PostMapping("/api/cards/validate")
    Map<String, Object> checkCardValidity(@RequestBody Map<String,Object> cardDTO);

}
