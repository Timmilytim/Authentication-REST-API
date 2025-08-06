package com.auth.repository;

import com.auth.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository extends JpaRepository<AuthUser , Long> {
    <Optional> AuthUser findByEmailOrUsername(String email, String username);
    AuthUser findByEmail(String email);
}
