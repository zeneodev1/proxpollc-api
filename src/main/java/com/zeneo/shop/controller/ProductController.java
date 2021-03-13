package com.zeneo.shop.controller;

import com.zeneo.shop.persistance.entity.Product;
import com.zeneo.shop.persistance.entity.ProductDetails;
import com.zeneo.shop.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Mono<ProductDetails> getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }


    @GetMapping()
    public Flux<Product> getProducts(@RequestParam(name = "size", defaultValue = "16", required = false) int size,
                                     @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                     @RequestParam(name = "dId", required = false) String dId,
                                     @RequestParam(name = "cId", required = false) String cId,
                                     @RequestParam(name = "min", required = false) Integer minPrice,
                                     @RequestParam(name = "max", required = false) Integer maxPrice,
                                     @RequestParam(name = "condition", required = false) String condition,
                                     @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
                                     @RequestParam(name = "order", required = false, defaultValue = "ASC") String order
    ) {
        return productService.getProducts(size, page, dId, cId, minPrice, maxPrice, condition, sortBy, order);
    }

    @GetMapping("/count")
    public Mono<Object> getCount() {
        return null;
    }


    @GetMapping("/name/{name}")
    public Mono<ProductDetails> getProductByName(@PathVariable("name") String name) {
        return productService.getProductByName(name);
    }

    @GetMapping("/category/{id}")
    public Flux<Product> getProductsByCategory(@PathVariable String id, Pageable pageable) {
        return productService.getProductsByCategory(id, pageable);
    }

    @GetMapping("/department/{id}")
    public Flux<Product> getProductsByDepartment(@PathVariable String id, Pageable pageable) {
        return productService.getProductsByDepartment(id, pageable);
    }

    @PostMapping
    public Mono<ProductDetails> addProduct(@RequestBody ProductDetails product) {
        return productService.addProduct(product);
    }

    @PutMapping
    public Mono<ProductDetails> updateProduct(@RequestBody ProductDetails product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public Mono<Product> deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

}
