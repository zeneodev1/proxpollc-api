package com.zeneo.shop.persistance.repository;

import com.zeneo.shop.persistance.entity.Wish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface WishRepository extends ReactiveMongoRepository<Wish, String> {
    Flux<Wish> findByUserId(String id);
}
