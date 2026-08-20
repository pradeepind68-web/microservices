package com.order.service.order_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.service.order_service.controller.OrderController;
import com.order.service.order_service.dto.OrderDto;
import com.order.service.order_service.dto.Payload;
import com.order.service.order_service.dto.PaymentDto;
import com.order.service.order_service.dto.ProductDTO;
import com.order.service.order_service.entity.OrderDetails;
import com.order.service.order_service.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private RestTemplate restTemplate;

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


    public ResponseEntity<Payload> createOrder(OrderDto order){

        // Verify user
        String userUrl="http://localhost:8765/user-api/user/"+order.userId();
        ResponseEntity<Payload> userResponse= restTemplate.getForEntity(userUrl,Payload.class);
        if(userResponse.getBody()==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Payload(null,"Order failed User not found"));
        }
        // save order with pending status
        OrderDetails orderDetails= saveOrder(order);

        //get Product check quantity
        String url="http://localhost:8765/product-api/product/" + order.productId();
        ResponseEntity<Payload> payloadResponse= restTemplate.getForEntity(url,Payload.class);
        ProductDTO productDTO=null;
        if(payloadResponse.getBody()==null){
            OrderDetails failedOrder=OrderDetails.builder().status("FAILED_PRODUCT_NOT_FOUND").userId(order.userId()).quantity(order.quantity())
                    .orderId(orderDetails.getOrderId()).build();
            orderRepo.save(failedOrder);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Payload(null,"Order failed Product not found"));
        }else if(payloadResponse.getBody().response() instanceof LinkedHashMap<?,?> linkedHashMap){
            Payload payload =payloadResponse.getBody();
            ObjectMapper mapper = new ObjectMapper();
            productDTO = mapper.convertValue(linkedHashMap, ProductDTO.class);
        }
        //make payment
        assert productDTO != null;
        double amount=order.quantity()*productDTO.quantity()* productDTO.price();
        String paymentUrl="http://localhost:8765/payment-api/make/payment";
        PaymentDto paymentDto=new PaymentDto(null,orderDetails.getOrderId(),amount,"PENDING",order.cardDetails());
        Payload payload= restTemplate.postForEntity(paymentUrl,paymentDto,Payload.class).getBody();
        Map<String,Object> response=null;
        if(payload!=null && payload.response() instanceof Map<?,?> map){
            Long paymentId=(Long)map.get("productId");
            response = (Map<String, Object>) map;
            if(Boolean.TRUE.equals(map.get("valid"))) {
                response.put("Order", "Successfully Placed");
                OrderDetails successFulOrder = OrderDetails.builder().status("Completed").userId(order.userId()).quantity(order.quantity())
                        .orderId(orderDetails.getOrderId()).productId(productDTO.productId()).paymentId(paymentId).build();
                orderRepo.save(successFulOrder);
            }else{
                response.put("Order", "Order is not placed due to payment failure");
            }
        }

        return ResponseEntity.ok(new Payload(response,null));
    }
}
