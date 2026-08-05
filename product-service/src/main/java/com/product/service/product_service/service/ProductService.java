package com.product.service.product_service.service;


import com.product.service.product_service.dto.Payload;
import com.product.service.product_service.dto.ProductDTO;
import com.product.service.product_service.entity.Product;
import com.product.service.product_service.repo.ProductRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo){
        this.productRepo=productRepo;
    }

    public Payload saveProduct(ProductDTO productDTO){
        boolean isExist= isExist(productDTO.name());
        if(!isExist) {
            Product product = getProduct(productDTO);
            productRepo.save(product);
            return new Payload("Product Saved Successfully ",null);
        }else
            return new Payload(null,"Product with same name already exists");

    }

    public Payload saveAllProducts(List<ProductDTO> productDTOS){
        List<String> result=productDTOS.stream().map(productDTO-> {
            boolean exist= isExist(productDTO.name());
            if(exist)
                return "Product with same name :"+productDTO.name()+" already exists";
            else
                return null;
        }).toList();
        if(result.stream().allMatch(Objects::isNull)) {
            List<Product> products = productDTOS.stream().map(ProductService::getProduct).collect(Collectors.toList());
            productRepo.saveAll(products);
            return new Payload("Products Saved Successfully ",null);
        }else
            return new Payload(result.stream().filter(Objects::nonNull).toList(),null);

    }

    public String updateProduct(ProductDTO productDTO, Long id){
        Product pro= productRepo.findById(id).orElseThrow(()->new RuntimeException("Product not found"));
        Product product= updateProductDetails(pro,productDTO);
        productRepo.save(product);
        return "Product updated Successfully";
    }

    private static Product updateProductDetails(Product product, ProductDTO productDTO){
        return Product.builder().productId(product.getProductId())
                .name(productDTO.name()).price(productDTO.price()).
                quantity(productDTO.quantity()).build();
    }

    private static @NonNull Product getProduct(ProductDTO productDTO) {
        return Product.builder().productId(productDTO.productId())
                .name(productDTO.name()).price(productDTO.price()).
        quantity(productDTO.quantity()).build();
    }

    public boolean isExist(String name){
        return productRepo.existsByNameIgnoreCase(name);
    }
}
