package com.zeneo.shop.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        DecodedJWT decodedJwt = jwtUtil.verifyToken(authToken);

        String email = jwtUtil.getSubject(decodedJwt);

        if (email != null) {
            String role = jwtUtil.getClaim(decodedJwt, "role").asString();
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList(role);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities);
            return Mono.just(authenticationToken);
        } else {
            return Mono.empty();
        }

    }

}
