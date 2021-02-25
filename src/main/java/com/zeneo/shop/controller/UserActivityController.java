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


    @PostMapping("/{id}/wish")
    public Mono<Wish> addToWish(@PathVariable String id, @RequestBody Product product) {
        return wishRepository.save(new Wish(id, product));
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


    @DeleteMapping("/{id}/wish")
    public Mono<Void> removeFromWish(@PathVariable String id, @RequestBody Wish wish) {
        return wishRepository.delete(wish);
    }

    @DeleteMapping("/{userId}/cart/{id}")
    public Mono<Void> removeFromCart(@PathVariable String userId, @PathVariable String id) {
        return cartRepository.deleteById(id);
    }

}
