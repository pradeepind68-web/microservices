package com.product.service.product_service.repo;

import com.product.service.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    boolean existsByNameIgnoreCase(String name);
}
