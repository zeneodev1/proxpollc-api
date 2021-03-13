package com.zeneo.shop.persistance.repository;

import com.zeneo.shop.persistance.entity.ProductDetails;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductDetailsRepository extends ReactiveMongoRepository<ProductDetails, String> {
    Mono<ProductDetails> findFirstByShortCut(String shortCut);
}
