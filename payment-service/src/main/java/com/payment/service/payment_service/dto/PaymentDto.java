package com.payment.service.payment_service.dto;

public record PaymentDto(String paymentId, String orderId, double amount, String status,CardDTO cardDTO){
}
