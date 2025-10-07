package com.sample.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.sample.gatewayserver.service.UserService1;
import com.sample.gatewayserver.util.JwtUtils;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter, Ordered {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserService1 userService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            return Mono.justOrEmpty(jwtUtil.extractUsername(jwt))
                    .flatMap(username -> userService.loadUserByUsernameReactive(username)
                            .flatMap(userDetails -> {
                                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                                    UsernamePasswordAuthenticationToken authentication =
                                            new UsernamePasswordAuthenticationToken(
                                                    userDetails, null, userDetails.getAuthorities());
                                    return chain.filter(exchange)
                                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                                } else {
                                    logger.warn("JWT token validation failed for user: {}", username);
                                    return unauthorized(exchange);
                                }
                            }))
                    .switchIfEmpty(chain.filter(exchange)) // continue chain if username extraction fails
                    .onErrorResume(e -> {
                        logger.error("JWT processing error: {}", e.getMessage(), e);
                        return unauthorized(exchange);
                    });

        } else {
            // Let unauthenticated requests continue if your endpoint allows it
            return chain.filter(exchange);
        }
    }


    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Run BEFORE security filter chain
    }
}
