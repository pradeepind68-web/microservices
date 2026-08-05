package com.product.service.product_service.controller;


import com.product.service.product_service.dto.Payload;
import com.product.service.product_service.dto.ProductDTO;
import com.product.service.product_service.entity.Product;
import com.product.service.product_service.repo.ProductRepo;
import com.product.service.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product-api")
public class ProductController {

    private final ProductRepo productRepo;
    private final ProductService productService;

    public ProductController(ProductService productService,ProductRepo productRepo){
        this.productService=productService;
        this.productRepo=productRepo;
    }

    @GetMapping(value = "/product/{id}")
    public ResponseEntity<Payload> getProduct(@PathVariable Long id){

        Optional<Product> product= productRepo.findById(id);
        return product.map(details -> ResponseEntity.ok(new Payload(product,null)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Payload(null,"No Such Product Found")));
    }

    @GetMapping(value = "/products")
    public ResponseEntity<Payload> getProducts(){

        List<Product> products= productRepo.findAll();
        if (!products.isEmpty())
            return ResponseEntity.ok(new Payload(products,null));
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Payload(null,"There is no Product"));
    }

    @PostMapping(value = "/product")
    public ResponseEntity<Payload> saveProduct(@RequestBody ProductDTO productDTO){

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(productDTO));
    }

    @PostMapping(value = "/products")
    public ResponseEntity<Payload> saveProducts(@RequestBody List<ProductDTO> productDTOS){

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveAllProducts(productDTOS));
    }

    @PutMapping(value = "/product/{id}")
    public ResponseEntity<Payload> updateProduct(@RequestBody ProductDTO productDTO,@PathVariable Long id){

        return ResponseEntity.ok(new Payload(productService.updateProduct(productDTO,id),null));
    }

    @DeleteMapping(value = "/product/{id}")
    public ResponseEntity<Payload> deleteProduct(@PathVariable Long id){
        productRepo.deleteById(id);
        return ResponseEntity.ok(new Payload("Product Deleted",null));
    }
}
