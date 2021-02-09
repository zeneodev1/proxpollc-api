package com.zeneo.shop.controller;

import com.zeneo.shop.persistance.entity.Category;
import com.zeneo.shop.persistance.entity.Department;
import com.zeneo.shop.persistance.repository.CategoryRepository;
import com.zeneo.shop.persistance.repository.DepartmentRepository;
import com.zeneo.shop.persistance.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{id}")
    public Mono<Category> getCategory(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @GetMapping
    public Flux<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public Mono<Category> addCategory(@RequestBody Category category) {
        return categoryRepository.save(category)
                .doOnNext(category1 -> departmentRepository.findById(category1.getDepartmentId())
                .map(department -> {
                    if (department.getCategories() == null) {
                        department.setCategories(new ArrayList<>());
                    }
                    department.getCategories().add(category);
                    log.info("1 " + department.toString());
                    return department;
                })
                .doOnNext((d) -> {
                    log.info("2 " + d.toString());
                    departmentRepository.save(d).doOnNext(department -> {
                        log.info("3 " +  department.toString());
                    }).subscribe();
                })
                .subscribe());
    }

    @PutMapping
    public Mono<Category> updateCategory(@RequestBody Category category) {
        return categoryRepository.existsById(category.getId())
                .then(categoryRepository.save(category));
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteCategory(@PathVariable String id) {
        return categoryRepository
                .deleteById(id)
                .doOnNext(aVoid -> {
                    productRepository.deleteAllByCategoryId(id).subscribe();
                });
    }

}
