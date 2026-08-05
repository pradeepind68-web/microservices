package com.ms.auth.service.auth_service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    @GetMapping("/user-api/findByUsername/{username}")
    ResponseEntity<Boolean> findByUsername(@PathVariable String username);
}
