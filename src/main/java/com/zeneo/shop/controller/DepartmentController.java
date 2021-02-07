package com.zeneo.shop.controller;

import com.zeneo.shop.persistance.entity.Category;
import com.zeneo.shop.persistance.entity.Department;
import com.zeneo.shop.persistance.repository.CategoryRepository;
import com.zeneo.shop.persistance.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    public Mono<Department> getDepartment(@PathVariable String id) {
        return departmentRepository.findById(id);
    }


    @GetMapping("/{id}/categories")
    public Flux<Category> getDepartmentCategories(@PathVariable String id) {
        return categoryRepository.findAllByDepartmentId(id);
    }

    @GetMapping
    public Flux<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    @PostMapping
    public Mono<Department> addDepartment(@RequestBody Department department) {
        return departmentRepository.save(department);
    }

    @PutMapping
    public Mono<Department> updateDepartment(@RequestBody Department department) {
        return departmentRepository.existsById(department.getId())
                .then(departmentRepository.save(department));
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteDepartment(@PathVariable String id) {
        return departmentRepository.deleteById(id);
    }

}
