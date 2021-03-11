package com.zeneo.shop.controller;

import com.mongodb.client.result.UpdateResult;
import com.zeneo.shop.persistance.entity.Category;
import com.zeneo.shop.persistance.entity.Department;
import com.zeneo.shop.persistance.entity.Product;
import com.zeneo.shop.persistance.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ReactiveMongoTemplate mongoTemplate;


    private void increaseDepartment(String id) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(id)), new Update().inc("productCount", 1), Department.class).subscribe();
    }

    private void increaseCategory(String id) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(id)), new Update().inc("productCount", 1), Category.class).subscribe();
    }
    
    private void decreaseDepartment(String id) {
        mongoTemplate
                .updateFirst(Query.query(Criteria.where("id").is(id)), new Update().inc("productCount", -1), Department.class)
                .subscribe();
    }

    private void decreaseCategory(String id) {
        mongoTemplate
                .updateFirst(Query.query(Criteria.where("id").is(id)), new Update().inc("productCount", -1), Category.class)
                .subscribe();
    }

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable String id) {
        return productRepository.findById(id);
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
        Criteria criteria = new Criteria();
        if (dId != null)
            criteria.and("departmentId").is(dId);
        if (cId != null)
            criteria.and("categoryId").is(cId);
        if (minPrice != null && maxPrice != null)
            criteria.and("price").gte(minPrice).lte(maxPrice);
        else if (maxPrice != null)
            criteria.and("price").lte(maxPrice);
        else if (minPrice != null)
            criteria.and("price").gte(minPrice);
        if (condition != null)
            criteria.and("condition").is(condition);
        Query query = Query.query(criteria);
        Sort sort = null;
        query.with(PageRequest.of(page, size));
        if (order.equals("ASC")) {
            sort = Sort.by(Sort.Direction.ASC, sortBy);
            query.with(PageRequest.of(page, size, sort));
        } else if (order.equals("DESC")) {
            sort = Sort.by(Sort.Direction.DESC, sortBy);
            query.with(PageRequest.of(page, size, sort));
        }
        return mongoTemplate.find(query, Product.class);
    }

    @GetMapping("/count")
    public Mono<Object> getCount() {
        return null;
    }


    @GetMapping("/name/{name}")
    public Mono<Product> getProductByName(@PathVariable("name") String name) {
        return productRepository.findFirstByShortCut(name);
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
        product.setShortCut(product.getTitle().toLowerCase().replace(" ", "-"));
        return productRepository
                .save(product)
                .log("save product")
                .doOnNext((p) -> {
                    increaseCategory(p.getCategoryId());
                }).log("increase category")
                .doOnNext(p -> {
                    increaseDepartment(p.getDepartmentId());
                }).log("increase department");
    }

    @PutMapping
    public Mono<Product> updateProduct(@RequestBody Product product) {
        return productRepository.existsById(product.getId())
                .then(productRepository.save(product));
    }

    @DeleteMapping("/{id}")
    public Mono<Product> deleteProduct(@PathVariable String id) {
        return productRepository
                .findById(id)
                .doOnNext(product -> {
                    productRepository.delete(product).subscribe();
                }).doOnNext(product -> {
                    decreaseDepartment(product.getDepartmentId());
                }).doOnNext(product -> {
                    decreaseCategory(product.getCategoryId());
                });
    }

}
