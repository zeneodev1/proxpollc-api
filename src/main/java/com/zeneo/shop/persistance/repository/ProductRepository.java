package com.zeneo.shop.persistance.repository;

import com.zeneo.shop.persistance.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ProductRepository extends ReactiveSortingRepository<Product, String> {

    Flux<Product> findAllByCategoryId(String id ,final Pageable pageable);
    Flux<Product> findAllByDepartmentId(String categoryId, Pageable pageable);
    Flux<Product> findAllByIdNotNull(Pageable pageable);

}
