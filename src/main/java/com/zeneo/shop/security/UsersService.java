package com.zeneo.shop.security;

import com.zeneo.shop.persistance.entity.User;
import com.zeneo.shop.persistance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    public Mono<User> findByUsername(String s) {
        return userRepository.findUserByEmail(s);
    }

}
