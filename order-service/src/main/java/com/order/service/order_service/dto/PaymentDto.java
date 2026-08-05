package com.order.service.order_service.dto;

public record PaymentDto(Long paymentId, Long orderId, double amount, String status,CardDTO cardDTO){
}
