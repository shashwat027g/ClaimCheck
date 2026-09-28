package com.claimcheck.backend.controller;

import com.claimcheck.backend.dto.LoginRequest;
import com.claimcheck.backend.dto.RegisterRequest;
import com.claimcheck.backend.dto.UpdateProfileRequest;
import com.claimcheck.backend.dto.ChangePasswordRequest;
import com.claimcheck.backend.entity.User;
import com.claimcheck.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        origins = {
                "http://127.0.0.1:5500",
                "http://localhost:5500",
                "http://127.0.0.1:3000",
                "http://localhost:3000"
        }
)
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder =
                new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "EMAIL_EXISTS",
                            "message",
                            "An account with this email already exists."
                    ));
        }

        String hashedPassword =
                passwordEncoder.encode(
                        request.getPassword()
                );

        User user = new User(
                request.getName(),
                request.getEmail(),
                hashedPassword
        );

        User savedUser =
                userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message",
                        "Account created successfully.",
                        "userId",
                        savedUser.getId(),
                        "name",
                        savedUser.getName(),
                        "email",
                        savedUser.getEmail()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        User user =
                userRepository.findByEmail(
                        request.getEmail()
                ).orElse(null);

        if (user == null
                || !passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                )) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "INVALID_CREDENTIALS",
                            "message",
                            "Invalid email or password."
                    ));
        }

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Login successful.",
                        "userId",
                        user.getId(),
                        "name",
                        user.getName(),
                        "email",
                        user.getEmail()
                )
        );
    }

    @PutMapping("/profile/{userId}")
public ResponseEntity<?> updateProfile(
        @PathVariable Long userId,
        @Valid @RequestBody UpdateProfileRequest request) {

    User user = userRepository.findById(userId).orElse(null);

    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "USER_NOT_FOUND",
                        "message", "User account not found."
                ));
    }

    User existingUser =
            userRepository.findByEmail(request.getEmail()).orElse(null);

    if (existingUser != null
            && !existingUser.getId().equals(userId)) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "EMAIL_EXISTS",
                        "message", "This email is already used by another account."
                ));
    }

    user.setName(request.getName());
    user.setEmail(request.getEmail());

    User updatedUser = userRepository.save(user);

    return ResponseEntity.ok(
            Map.of(
                    "message", "Profile updated successfully.",
                    "userId", updatedUser.getId(),
                    "name", updatedUser.getName(),
                    "email", updatedUser.getEmail()
            )
    );
}

@PutMapping("/password/{userId}")
public ResponseEntity<?> changePassword(
        @PathVariable Long userId,
        @Valid @RequestBody ChangePasswordRequest request) {

    User user = userRepository.findById(userId).orElse(null);

    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "USER_NOT_FOUND",
                        "message", "User account not found."
                ));
    }

    if (!passwordEncoder.matches(
            request.getCurrentPassword(),
            user.getPassword()
    )) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "INVALID_PASSWORD",
                        "message", "Current password is incorrect."
                ));
    }

    if (passwordEncoder.matches(
            request.getNewPassword(),
            user.getPassword()
    )) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "SAME_PASSWORD",
                        "message", "New password must be different from the current password."
                ));
    }

    String newHashedPassword =
            passwordEncoder.encode(request.getNewPassword());

    user.setPassword(newHashedPassword);

    userRepository.save(user);

    return ResponseEntity.ok(
            Map.of(
                    "message", "Password changed successfully."
            )
    );
}

}