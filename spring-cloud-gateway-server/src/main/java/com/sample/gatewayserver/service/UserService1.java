package com.sample.gatewayserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sample.gatewayserver.model.User;
import com.sample.gatewayserver.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserService1 implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService1.class);

    @Autowired
    public UserService1(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
        	logger.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
    
    // Reactive method to fetch UserDetails
    public Mono<UserDetails> loadUserByUsernameReactive(String username) {
        // Replace this with reactive call if using DB (like R2DBC)
        return Mono.fromCallable(() -> loadUserByUsername(username))
                   .flatMap(userDetails -> {
                       if (userDetails != null) {
                           return Mono.just(userDetails);
                       } else {
                           return Mono.error(new UsernameNotFoundException("User not found: " + username));
                       }
                   });
    }
}