package com.zeneo.shop.persistance.repository;

import com.zeneo.shop.persistance.entity.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    
    Flux<Category> findAllByDepartmentId(String departmentId);
    
}
