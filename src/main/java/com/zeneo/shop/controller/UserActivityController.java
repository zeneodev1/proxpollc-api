package com.zeneo.shop.controller;

import com.zeneo.shop.persistance.entity.CartItem;
import com.zeneo.shop.persistance.entity.Product;
import com.zeneo.shop.persistance.entity.UserActivity;
import com.zeneo.shop.persistance.repository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("userActivity")
public class UserActivityController {

    @Autowired
    private UserActivityRepository userActivityRepository;

    @GetMapping("{id}")
    public Mono<UserActivity> getUserActivity(@PathVariable String id) {
        return userActivityRepository
                .findUserActivity(id)
                .switchIfEmpty(userActivityRepository.createUserActivity(id));
    }

    @PostMapping("/{id}/wish")
    public Mono<Void> addToWish(@PathVariable String id, @RequestBody Product product) {
        return userActivityRepository.addToWish(product, id).then();
    }

    @PostMapping("/{id}/cart")
    public Mono<Void> addToCart(@PathVariable String id, @RequestBody CartItem cartItem) {
        return userActivityRepository.addToCart(cartItem, id).then();
    }

    @DeleteMapping("/{id}/wish")
    public Mono<Void> removeFromWish(@PathVariable String id, @RequestBody Product product) {
        return userActivityRepository.removeFromWish(product, id).then();
    }

    @DeleteMapping("/{id}/cart")
    public Mono<Void> removeFromCart(@PathVariable String id, @RequestBody CartItem cartItem) {
        return userActivityRepository.removeFromCart(cartItem, id).then();
    }

}
