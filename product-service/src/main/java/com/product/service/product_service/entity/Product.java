package com.product.service.product_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long productId;
    private String name;
    private Double price;
    private Integer quantity;

    public Product(){}

    public Product(ProductBuilder productBuilder){
        this.productId=productBuilder.productId;
        this.name=productBuilder.name;
        this.price=productBuilder.price;
        this.quantity=productBuilder.quantity;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public Long getProductId() {
        return productId;
    }

    public static ProductBuilder builder(){ return new ProductBuilder();}

    public static class ProductBuilder{
        private Long productId;
        private String name;
        private Double price;
        private Integer quantity;


        public ProductBuilder productId(Long productId){
            this.productId=productId;
            return this;
        }
        public ProductBuilder name(String name){
            this.name=name;
            return this;
        }
        public ProductBuilder price(Double price){
            this.price=price;
            return this;
        }
        public ProductBuilder quantity(Integer quantity){
            this.quantity=quantity;
            return this;
        }
        public Product build() {
            if (this.name == null || this.name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name can not be null or empty");
            }
            return new Product(this);
        }

    }
}
