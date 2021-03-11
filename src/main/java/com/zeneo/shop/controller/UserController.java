package com.zeneo.shop.controller;

import com.zeneo.shop.model.ChangePassword;
import com.zeneo.shop.model.EditUserRequest;
import com.zeneo.shop.model.LoginRequest;
import com.zeneo.shop.model.ResponseUser;
import com.zeneo.shop.persistance.entity.User;
import com.zeneo.shop.persistance.repository.UserRepository;
import com.zeneo.shop.security.JwtUtil;
import com.zeneo.shop.security.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/users")
public class UserController {

    private final static ResponseEntity<Object> UNAUTHORIZED =
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    private final static ResponseEntity<Object> OK =
            ResponseEntity.status(HttpStatus.OK).build();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;
    @Autowired
    private UsersService usersService;

    @PostMapping(value = "/login", produces = {MediaType.TEXT_PLAIN_VALUE})
    private Mono<ResponseEntity<?>> login (@RequestBody LoginRequest loginRequest) {
        return Mono.just(loginRequest)
                .flatMap((loginRequest1) -> usersService.findByUsername(loginRequest.getEmail())
                .map((userDetails -> passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())
                        ? ResponseEntity.ok(jwtUtil.generateToken(userDetails))
                        : UNAUTHORIZED)))
                .defaultIfEmpty(UNAUTHORIZED);
    }


    @PutMapping(value = "/changePassword")
    private Mono<ResponseEntity<Object>> changePassword (@RequestBody ChangePassword changePassword) {
        return Mono.just(changePassword)
                .flatMap((loginRequest1) -> usersService.findByUsername(changePassword.getEmail())
                    .map((user -> {
                        boolean matched = passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword());
                        if (matched) {
                             user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
                             userRepository.save(user).subscribe();
                             return OK;
                        } else {
                            return UNAUTHORIZED;
                        }
                    })))
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

    @GetMapping("/{id}")
    private Mono<ResponseUser> getInfo (@PathVariable String id) {
        return userRepository.findById(id).map(User::toResponseUser);
    }

    @PutMapping("/{id}")
    private Mono<User> editInfo (
            @RequestBody EditUserRequest editUserRequest,
            @PathVariable String id,
            @RequestParam(name = "changeEmail", required = false, defaultValue = "false") Boolean changeEmail,
            @RequestParam(name = "changePass", required = false, defaultValue = "false") Boolean changePass) {
        return userRepository.findById(id).doOnNext(user -> {
            Query query = Query.query(Criteria.where("id").is(id));
            Update update = Update.update("firstName", editUserRequest.getFirstName());
            update.set("lastName", editUserRequest.getLastName());
            if (changeEmail)
                update.set("email", editUserRequest.getEmail());
            if (changePass) {
                boolean matched = passwordEncoder.matches(editUserRequest.getCurrentPassword(), user.getPassword());
                if (matched) {
                    update.set("password", passwordEncoder.encode(editUserRequest.getNewPassword()));
                }
            }


            mongoTemplate.findAndModify(query, update, User.class).subscribe();
        });
    }

}
