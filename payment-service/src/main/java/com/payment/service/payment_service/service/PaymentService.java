package com.payment.service.payment_service.service;

import com.payment.service.payment_service.dto.PaymentDto;
import com.payment.service.payment_service.entity.Payment;
import com.payment.service.payment_service.proxy.CardServiceProxy;
import com.payment.service.payment_service.repo.PaymentRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PaymentService {

    private final RestTemplate restTemplate;
    private final PaymentRepo paymentRepo;
    private final CardServiceProxy cardServiceProxy;

    public PaymentService(RestTemplate restTemplate,PaymentRepo paymentRepo,CardServiceProxy cardServiceProxy){
        this.restTemplate = restTemplate;
        this.paymentRepo=paymentRepo;
        this.cardServiceProxy=cardServiceProxy;
    }

    public Map<String,Object> getPayment(PaymentDto paymentDto) {
        String cardNumber= paymentDto.cardDTO().number();
        String expiry= paymentDto.cardDTO().expiry();
        Payment tx = new Payment();
        tx.setOrderId(paymentDto.orderId());
        tx.setAmount(paymentDto.amount());

        Map<String,Object> compliance = cardServiceProxy.checkCardValidity(Map.of("cardNumber",cardNumber,"expiry",expiry));
        if(compliance != null && Boolean.TRUE.equals(compliance.get("valid"))){
            tx.setStatus("SUCCESSFUL");
            compliance.put("Payment","Successful");
        }
        else{
            tx.setStatus("DECLINED");
        }
        paymentRepo.save(tx);
        assert compliance != null;
        compliance.put("paymentId",tx.getPaymentId());
        return compliance;
    }
}
