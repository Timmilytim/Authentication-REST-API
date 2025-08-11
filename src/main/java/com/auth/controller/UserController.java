package com.auth.controller;

import com.auth.model.User;
import com.auth.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
   private AuthUserRepository authUserRepository;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        // Get the authenticated user's email or username
        String emailOrUsername = authentication.getName();

        User user = authUserRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        // Return the user details in the response
        Map<String, Object> userDetails = Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "email", user.getEmail(),
                "username", user.getUsername()
        );
        return ResponseEntity.ok(userDetails);
    }
}
