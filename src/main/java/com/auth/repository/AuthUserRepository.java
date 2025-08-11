package com.auth.repository;

import com.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository extends JpaRepository<User, Long> {
    <Optional> User findByEmailOrUsername(String email, String username);
    User findByEmail(String email);
}
