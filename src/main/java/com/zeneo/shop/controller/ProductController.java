package com.zeneo.shop.controller;

import com.zeneo.shop.persistance.entity.Product;
import com.zeneo.shop.persistance.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable String id) {
        return productRepository.findById(id);
    }


    @GetMapping()
    public Flux<Product> getProducts(Pageable pageable) {
        return productRepository.findAllByIdNotNull(pageable);
    }


    @GetMapping("/category/{id}")
    public Flux<Product> getProductsByCategory(@PathVariable String id, Pageable pageable) {
        return productRepository.findAllByCategoryId(id, pageable);
    }


    @GetMapping("/department/{id}")
    public Flux<Product> getProductsByDepartment(@PathVariable String id, Pageable pageable) {
        return productRepository.findAllByDepartmentId(id, pageable);
    }


    @PostMapping
    public Mono<Product> addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping
    public Mono<Product> updateProduct(@RequestBody Product product) {
        return productRepository.existsById(product.getId())
                .then(productRepository.save(product));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productRepository.deleteById(id).subscribe();
    }

}
