package com.order.service.order_service.controller;

import com.order.service.order_service.dto.*;
import com.order.service.order_service.repo.OrderRepo;
import com.order.service.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order-service")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepo orderRepo;

    @GetMapping(value = "/order/{userId}")
    public ResponseEntity<Payload> getOrderStatus(@PathVariable Long userId) {
        return null;
    }

    @PostMapping(value = "/order")
    public ResponseEntity<Payload> createOrder(@RequestBody OrderDto order) {
        return orderService.createOrder(order);
    }
}
