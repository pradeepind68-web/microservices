package com.order.service.order_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.service.order_service.dto.*;
import com.order.service.order_service.entity.OrderDetails;
import com.order.service.order_service.repo.OrderRepo;
import com.order.service.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/order-service")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/order/{userId}")
    public ResponseEntity<Payload> getOrderStatus(@PathVariable Long userId){
        return null;
    }

    @PostMapping(value = "/order")
    public ResponseEntity<Payload> createOrder(@RequestBody OrderDto order){

        // Verify user
        String userUrl="http://localhost:8765/user-api/user/"+order.userId();
        ResponseEntity<Payload> userResponse=restTemplate.getForEntity(userUrl,Payload.class);
        if(userResponse.getBody()==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Payload(null,"Order failed User not found"));
        }
        // save order with pending status
        OrderDetails orderDetails=orderService.saveOrder(order);

        //get Product check quantity
        String url="http://localhost:8765/product-api/product/" + order.productId();



        ResponseEntity<Payload> payloadResponse=restTemplate.getForEntity(url,Payload.class);
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
        Double amount=order.quantity()*productDTO.quantity()* productDTO.price();

        String paymentUrl="http://localhost:8765/payment-api/make/payment";
        PaymentDto paymentDto=new PaymentDto(null,orderDetails.getOrderId(),amount,"PENDING",order.cardDetails());
        Payload payload=restTemplate.postForEntity(paymentUrl,paymentDto,Payload.class).getBody();
        Map<String,Object> response=null;
        if(payload!=null && payload.response() instanceof Map<?,?> map){
            Long paymentId=(Long)map.get("productId");
            response= (Map<String, Object>) map;
            response.put("Order","Successfully Placed");
            OrderDetails successFulOrder=OrderDetails.builder().status("Completed").userId(order.userId()).quantity(order.quantity())
                    .orderId(orderDetails.getOrderId()).productId(productDTO.productId()).paymentId(paymentId).build();
            orderRepo.save(successFulOrder);
        }

        return ResponseEntity.ok(new Payload(response,null));
    }

}
