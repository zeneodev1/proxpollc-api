package com.zeneo.shop.controller;

import com.zeneo.shop.model.LoginRequest;
import com.zeneo.shop.model.ResponseUser;
import com.zeneo.shop.persistance.entity.User;
import com.zeneo.shop.persistance.repository.UserRepository;
import com.zeneo.shop.security.JwtUtil;
import com.zeneo.shop.security.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final static ResponseEntity<Object> UNAUTHORIZED =
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersService usersService;

    @PostMapping(value = "/login", produces = {MediaType.TEXT_PLAIN_VALUE})
    private Mono<ResponseEntity<?>> login (@RequestBody LoginRequest loginRequest) {
        return Mono.just(loginRequest).flatMap((loginRequest1) -> usersService.findByUsername(loginRequest.getEmail())
                .map((userDetails -> passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())
                        ? ResponseEntity.ok(jwtUtil.generateToken(userDetails))
                        : UNAUTHORIZED)))
                .defaultIfEmpty(UNAUTHORIZED);
    }

    @PostMapping("/register")
    private Mono<ResponseUser> register (@RequestBody User user) {
        return Mono.just(user).map(user1 -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return user;
        }).then(userRepository.save(user))
                .map(User::toResponseUser);
    }


}
