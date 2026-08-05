package com.order.service.order_service.service;

import com.order.service.order_service.dto.OrderDto;
import com.order.service.order_service.dto.Payload;
import com.order.service.order_service.entity.OrderDetails;
import com.order.service.order_service.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    public Payload getOrderStatus(Long orderId){

        return orderRepo.findById(orderId).map(order->(new Payload(order.getStatus(),null)))
                .orElseGet(()->new Payload(null,"No Order Found with Order Id: "+orderId));

    }

    public OrderDetails saveOrder(OrderDto order){

        OrderDetails orderDetails=OrderDetails.builder().status("PENDING").userId(order.userId()).quantity(order.quantity())
                .productId(order.productId()).build();
        orderRepo.save(orderDetails);
        return orderDetails;
    }
}
