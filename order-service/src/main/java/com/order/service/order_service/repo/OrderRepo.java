package com.order.service.order_service.repo;

import com.order.service.order_service.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<OrderDetails,Long> {

}
