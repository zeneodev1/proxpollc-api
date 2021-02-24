package com.zeneo.shop.persistance.repository;

import com.zeneo.shop.persistance.entity.CartItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CartRepository extends ReactiveMongoRepository<CartItem, String> {
    Flux<CartItem> findByUserId(String userId);
}
