package com.auth.controller;

import com.auth.dto.LoginRequest;
import com.auth.dto.ResetRequest;
import com.auth.dto.SignupRequest;
import com.auth.dto.VerifyOtpRequest;
import com.auth.model.User;
import com.auth.repository.AuthUserRepository;
import com.auth.service.EmailService;
import com.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint to handle user registration
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        // Check if the user already exists by email or username
        if (authUserRepository.findByEmailOrUsername(signupRequest.getEmail(), signupRequest.getUsername()) != null) {
            return new ResponseEntity<>("User already exists with this email or username", HttpStatus.BAD_REQUEST);
        }

        // Create a new user entity and set its properties
        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword())); // Encrypt the password
        user.setVerified(false); // Set the user as not verified initially

//        Generate Verification OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        authUserRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);


        // Save the new user to the repository
        authUserRepository.save(user);

        // Custom response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Check your email for OTP verification");
        response.put("email", user.getEmail());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // Endpoint to verify user email
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        User user = authUserRepository.findByEmail(request.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        if (user.isVerified()) {
            // Optionally regenerate token or just return a valid one
            return ResponseEntity.ok(Map.of(
                    "message", "User already verified"
            ));
        }

        if (user.getOtp().equals(request.getOtp()) && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
            user.setVerified(true);
            user.setOtp(null);
            user.setOtpExpiry(null);
            authUserRepository.save(user);


            return ResponseEntity.ok(Map.of(
                    "message", "OTP verified successfully"
            ));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid or expired OTP"));
    }


    // Endpoint to handle user login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = authUserRepository.findByEmailOrUsername(loginRequest.getEmail_or_username(),
                loginRequest.getEmail_or_username());

        // Check if user exists and password matches
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        // Check if user is verified
        if (!user.isVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "User not verified"));
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "token", token
        ));
    }

    // Endpoint to handle forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody VerifyOtpRequest request) {
        User user = authUserRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        // Generate OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        authUserRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);

        return ResponseEntity.ok(Map.of(
                "message", "OTP sent, check your email",
                "email", user.getEmail()
        ));
    }

    // Endpoint to verify OTP for password reset
    @PostMapping("/verify-reset-otp")
    public ResponseEntity<?> verifyResetOtp(@RequestBody VerifyOtpRequest request) {
        User user = authUserRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
        if (user.getOtp() != null && user.getOtp().equals(request.getOtp()) && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
            // OTP is valid,
            user.setOtp(null);
            user.setOtpExpiry(null);
            user.setResetPasswordAllowed(true);
            authUserRepository.save(user);

            return ResponseEntity.ok(Map.of("message", "OTP verified successfully, you can now reset your password"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid or expired OTP"));
    }


    // Endpoint to reset password
    @PostMapping("/reset-password")
    public ResponseEntity <?> resetPassword(@RequestBody ResetRequest resetRequest) {
        User user = authUserRepository.findByEmail(resetRequest.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        // Check if the user is verified
        boolean resetAllowed = user.getResetPasswordAllowed();
        if (!resetAllowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "OTP verification required"));
        }

        // Update the user password
        user.setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
        user.setResetPasswordAllowed(false);
        user.setOtp(null); // clear OTP from DB for safety
        user.setOtpExpiry(null); // clear expiry too
        authUserRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }

}
