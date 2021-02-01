package com.zeneo.shop.persistance.repository;

import com.zeneo.shop.persistance.entity.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DepartmentRepository extends ReactiveMongoRepository<Department, String> {
}
