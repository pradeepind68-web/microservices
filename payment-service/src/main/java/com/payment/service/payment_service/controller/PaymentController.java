package com.payment.service.payment_service.controller;

import com.payment.service.payment_service.dto.Payload;
import com.payment.service.payment_service.dto.PaymentDto;
import com.payment.service.payment_service.entity.Payment;
import com.payment.service.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment-api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/make/payment")
    public ResponseEntity<Payload> makePayment(@RequestBody PaymentDto paymentDto){

        Map<String,Object> res= paymentService.getPayment(paymentDto);
        return ResponseEntity.ok(new Payload(res,null));
    }

    @GetMapping(value = "/check/payment")
    public ResponseEntity<Payload> getPaymentStatus(){

        return ResponseEntity.ok(new Payload(null,"Application is running"));
    }


}
