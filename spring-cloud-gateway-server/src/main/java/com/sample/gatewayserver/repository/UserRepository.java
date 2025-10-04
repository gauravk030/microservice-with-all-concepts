package com.sample.gatewayserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sample.gatewayserver.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

