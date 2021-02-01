package com.zeneo.shop.security;

import com.zeneo.shop.persistance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class UserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        return userRepository.findUserByEmail(s).flatMap(u -> {
            User user = new User(u.getEmail(),
                    u.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority(u.getRole().toString())));
            return Mono.just(user);
        });
    }

}
