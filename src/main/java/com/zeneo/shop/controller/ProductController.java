package com.zeneo.shop.controller;

import com.mongodb.client.result.UpdateResult;
import com.zeneo.shop.persistance.entity.Category;
import com.zeneo.shop.persistance.entity.Department;
import com.zeneo.shop.persistance.entity.Product;
import com.zeneo.shop.persistance.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ReactiveMongoTemplate mongoTemplate;


    private void increaseDepartment(String id) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(id)), new Update().inc("productCount", 1), Department.class);
    }

    private void increaseCategory(String id) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(id)), new Update().inc("productCount", 1), Category.class);
    }
    
    private void decreaseDepartment(String id) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(id)), new Update().inc("productCount", -1), Department.class);
    }

    private void decreaseCategory(String id) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(id)), new Update().inc("productCount", -1), Category.class);
    }

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable String id) {
        return productRepository.findById(id);
    }


    @GetMapping()
    public Flux<Product> getProducts(@RequestParam(name = "size", defaultValue = "20", required = false) int size,
                                     @RequestParam(name = "page", defaultValue = "1", required = false) int page) {
        return productRepository.findAllByIdNotNull(PageRequest.of(size, page));
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
        return productRepository
                .save(product)
                .doOnNext((p) -> {
                    increaseCategory(p.getCategoryId());
                })
                .doOnNext(p -> {
                    increaseDepartment(p.getDepartmentId());
                });
    }

    @PutMapping
    public Mono<Product> updateProduct(@RequestBody Product product) {
        return productRepository.existsById(product.getId())
                .then(productRepository.save(product));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productRepository.findById(id).map(product ->
                productRepository.deleteById(id)
                .doOnNext((v) -> {
                    decreaseCategory(product.getCategoryId());
                }).doOnNext((v) -> {
                    decreaseDepartment(product.getDepartmentId());
                })
        ).subscribe();

    }

}
