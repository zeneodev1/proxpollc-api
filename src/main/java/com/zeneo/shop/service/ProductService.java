package com.zeneo.shop.service;

import com.zeneo.shop.persistance.entity.Category;
import com.zeneo.shop.persistance.entity.Department;
import com.zeneo.shop.persistance.entity.Product;
import com.zeneo.shop.persistance.entity.ProductDetails;
import com.zeneo.shop.persistance.repository.ProductDetailsRepository;
import com.zeneo.shop.persistance.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

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


    public Mono<ProductDetails> getProduct(String id) {
        return productDetailsRepository.findById(id);
    }


    public Flux<Product> getProducts(int size,
                                     int page,
                                     String dId,
                                     String cId,
                                     Integer minPrice,
                                     Integer maxPrice,
                                     String condition,
                                     String sortBy,
                                     String order
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

    public Mono<Object> getCount() {
        return null;
    }


    public Mono<ProductDetails> getProductByName(String name) {
        return productDetailsRepository.findFirstByShortCut(name);
    }

    public Flux<Product> getProductsByCategory(String id, Pageable pageable) {
        return productRepository.findAllByCategoryId(id, pageable);
    }

    public Flux<Product> getProductsByDepartment(String id, Pageable pageable) {
        return productRepository.findAllByDepartmentId(id, pageable);
    }

    public Mono<ProductDetails> addProduct(ProductDetails product) {
        product.setShortCut(product.getTitle().toLowerCase().replace(" ", "-"));
        return mongoTemplate.save(product.getProductDescription())
                .doOnNext(product::setProductDescription)
                .then(productDetailsRepository
                    .save(product)
                    .log("save product")
                    .doOnNext((p) -> {
                        increaseCategory(p.getCategoryId());
                    }).log("increase category")
                    .doOnNext(p -> {
                        increaseDepartment(p.getDepartmentId());
                    }).log("increase department"));
    }

    public Mono<ProductDetails> updateProduct(ProductDetails product) {
        return productRepository
                .findById(product.getId())
                .then(mongoTemplate.save(product.getProductDescription()))
                .map(productDescription -> {
                    product.setProductDescription(productDescription);
                    return product;
                })
                .then(productDetailsRepository.save(product));
    }

    public Mono<Product> deleteProduct(String id) {
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
