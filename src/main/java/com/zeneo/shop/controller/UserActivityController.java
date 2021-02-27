package com.zeneo.shop.controller;

import com.zeneo.shop.persistance.entity.CartItem;
import com.zeneo.shop.persistance.entity.Product;
import com.zeneo.shop.persistance.entity.Wish;
import com.zeneo.shop.persistance.repository.CartRepository;
import com.zeneo.shop.persistance.repository.UserActivityRepository;
import com.zeneo.shop.persistance.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("userActivity")
public class UserActivityController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private WishRepository wishRepository;

    @GetMapping("/{id}/cart")
    public Flux<CartItem> getAllCart(@PathVariable String id) {
        return cartRepository.findByUserId(id);
    }


    @GetMapping("/{id}/wish")
    public Flux<Wish> getAllWishes(@PathVariable String id) {
        return wishRepository.findByUserId(id);
    }


    @PostMapping("/{id}/wish")
    public Mono<Wish> addToWish(@PathVariable String id, @RequestBody Wish wish) {
        wish.setUserId(id);
        return wishRepository.save(wish);
    }

    @PostMapping("/{id}/cart")
    public Mono<CartItem> addToCart(@PathVariable String id, @RequestBody CartItem cartItem) {
        cartItem.setUserId(id);
        return cartRepository.save(cartItem);
    }


    @PostMapping("/{id}/cart/all")
    public Flux<CartItem> addAllToCart(@PathVariable String id, @RequestBody List<CartItem> cartItems) {
        cartItems.forEach(cartItem -> {
            cartItem.setUserId(id);
        });
        return cartRepository.saveAll(cartItems);
    }


    @PostMapping("/{id}/wish/all")
    public Flux<Wish> addAllToWish(@PathVariable String id, @RequestBody List<Wish> wishes) {
        wishes.forEach(cartItem -> {
            cartItem.setUserId(id);
        });
        return wishRepository.saveAll(wishes);
    }

    @DeleteMapping("/{userId}/wish/{id}")
    public Mono<Void> removeFromWish(@PathVariable String userId, @PathVariable String id) {
        return wishRepository.deleteById(id);
    }

    @DeleteMapping("/{userId}/cart/{id}")
    public Mono<Void> removeFromCart(@PathVariable String userId, @PathVariable String id) {
        return cartRepository.deleteById(id);
    }

}
