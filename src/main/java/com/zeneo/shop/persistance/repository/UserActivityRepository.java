package com.zeneo.shop.persistance.repository;

import com.mongodb.client.result.UpdateResult;
import com.zeneo.shop.persistance.entity.CartItem;
import com.zeneo.shop.persistance.entity.Product;
import com.zeneo.shop.persistance.entity.UserActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserActivityRepository {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<UserActivity> createUserActivity(String userId) {
        return reactiveMongoTemplate.save(new UserActivity(userId));
    }

    public Mono<UserActivity> findUserActivity(String userId) {
        return reactiveMongoTemplate
                .findOne(Query.query(Criteria.where("userId").is(userId)), UserActivity.class);
    }

    public Mono<UpdateResult> addToWish(Product product, String userId) {
        Update update = new Update();
        update.addToSet("wishList").value(product);
        return reactiveMongoTemplate
                .updateFirst(Query.query(Criteria.where("userId").is(userId)), update, UserActivity.class);
    }

    public Mono<UpdateResult> addToCart(CartItem cartItem, String userId) {
        Update update = new Update();
        update.addToSet("cartItems").value(cartItem);
        return reactiveMongoTemplate
                .updateFirst(Query.query(Criteria.where("userId").is(userId)), update, UserActivity.class);
    }


    public Mono<UpdateResult> removeFromWish(Product product, String userId) {
        Update update = new Update();
        update.pull("wishList", product);
        return reactiveMongoTemplate
                .updateFirst(Query.query(Criteria.where("userId").is(userId)), update, UserActivity.class);
    }

    public Mono<UpdateResult> removeFromCart(CartItem cartItem, String userId) {
        Update update = new Update();
        update.pull("cartItems", cartItem);
        return reactiveMongoTemplate
                .updateFirst(Query.query(Criteria.where("userId").is(userId)), update, UserActivity.class);
    }




}
