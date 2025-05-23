package com.springboot.eventlink.user.repository;

import com.springboot.eventlink.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);
    Optional<Users> findByEmail(String email);
    Users findByName(String name);
}
