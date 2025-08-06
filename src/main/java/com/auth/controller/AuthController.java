package com.auth.controller;

import com.auth.dto.SignupRequest;
import com.auth.model.AuthUser;
import com.auth.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint to handle user registration
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        // Check if the user already exists by email or username
        if (authUserRepository.findByEmailOrUsername(signupRequest.getEmail(), signupRequest.getUsername()) != null) {
            return new ResponseEntity<>("User already exists with this email or username", HttpStatus.BAD_REQUEST);
        }

        // Create a new user entity and set its properties
        AuthUser user = new AuthUser();
        newUser.setFirstName(signupRequest.getFirstName());
        newUser.setLastName(signupRequest.getLastName());
        newUser.setEmail(signupRequest.getEmail());
        newUser.setUsername(signupRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword())); // Encrypt the password
        newUser.setVerified(false); // Set the user as not verified initially

        // Save the new user to the repository
        authUserRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

}
