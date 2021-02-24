package com.zeneo.shop.persistance.repository;

import com.zeneo.shop.persistance.entity.Wish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface WishRepository extends ReactiveMongoRepository<Wish, String> {
}
