package com.order.service.order_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="order_details")
public class OrderDetails {

    @Id
    @GeneratedValue
    private Long orderId;
    private Integer userId;
    private Long productId;
    private Integer quantity;
    private Long paymentId;
    private String status;

    public OrderDetails(){}

    public Long getOrderId() {
        return orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Integer getQuantity() { return quantity; }

    public String getStatus() {
        return status;
    }

    public Long getProductId(){ return productId;}

    private OrderDetails(OrderBuilder orderBuilder){
        this.orderId=orderBuilder.orderId;
        this.userId=orderBuilder.userId;
        this.quantity=orderBuilder.quantity;
        this.paymentId=orderBuilder.paymentId;
        this.status=orderBuilder.status;
        this.productId=orderBuilder.productId;
    }

    public static OrderBuilder builder(){return new OrderBuilder();}

    public static class OrderBuilder{

        private Long orderId;
        private Integer userId;
        private Long productId;
        private Long paymentId;
        private Integer quantity;
        private String status;

        public OrderBuilder orderId(Long orderId){
            this.orderId=orderId;
            return this;
        }
        public OrderBuilder userId(Integer userId){
            this.userId=userId;
            return this;
        }
        public OrderBuilder paymentId(Long paymentId){
            this.paymentId=paymentId;
            return this;
        }
        public OrderBuilder status(String status){
            this.status=status;
            return this;
        }
        public OrderBuilder quantity(Integer quantity){
            this.quantity=quantity;
            return this;
        }
        public OrderBuilder productId(Long productId){
            this.productId=productId;
            return this;
        }

        public OrderDetails build(){
            if(this.userId==null || this.productId==null || this.quantity==null || this.quantity==0){
                throw new IllegalArgumentException("User id or Product or Quantity can not be null or empty");
            }
            return new OrderDetails(this);
        }

    }
}
