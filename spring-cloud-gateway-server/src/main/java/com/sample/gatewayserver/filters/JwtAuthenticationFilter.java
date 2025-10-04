package com.sample.gatewayserver.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.sample.gatewayserver.service.UserService1;
import com.sample.gatewayserver.util.JwtUtils;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserService1 userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst("Authorization");
        UsernamePasswordAuthenticationToken authentication = null;
        

        if (authHeader != null) {
            System.out.println("Authorization Header: " + authHeader);  // Log the header
        } else {
            System.out.println("No Authorization Header found");
        }

        

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    //SecurityContextHolder.getContext().setAuthentication(authentication);
                    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                }
            }
        }else if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Respond with 401 Unauthorized or handle as per your need
            return Mono.error(new IllegalArgumentException("Authorization header is missing or invalid"));
        }

        return chain.filter(exchange);
    }
    
//    @Override
//	public int getOrder() {
//	    return -1; // Run *after* SecurityWebFiltersOrder.AUTHENTICATION (which is 0)
//	}

}
