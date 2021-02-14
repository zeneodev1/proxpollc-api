package com.zeneo.shop.persistance.repository;

import com.zeneo.shop.persistance.entity.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface DepartmentRepository extends ReactiveMongoRepository<Department, String> {
    Mono<Department> findFirstByNameIsLike(String name);
}
