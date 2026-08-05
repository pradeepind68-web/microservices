package com.order.service.order_service.dto;


public record OrderDto(Integer userId, Long productId,Integer quantity,CardDTO cardDetails,String status) {
}
