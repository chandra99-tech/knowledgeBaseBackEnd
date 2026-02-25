package com.example.knowledgeplatform.controller;

import com.example.knowledgeplatform.entity.User;
import com.example.knowledgeplatform.service.UserService;
import com.example.knowledgeplatform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("Server is running");
    }

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String email = userData.get("email");
        String password = userData.get("password");

        if (username == null || email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username, email, and password are required"));
        }

        username = username.trim();
        email = email.trim();
        // We do NOT trim password to allow intentional spaces

        System.out.println("Signup attempt for email: " + email + ", username: " + username);
        try {
            User user = userService.registerUser(username, email, password);
            System.out.println("User created successfully with ID: " + user.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Signup failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required"));
        }

        email = email.trim();
        System.out.println("Login attempt for email: " + email);

        User user = userService.findByEmail(email);
        if (user == null) {
            System.out.println("User not found for email: " + email);
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }

        System.out.println("User found: " + user.getEmail());
        boolean passwordMatches = userService.checkPassword(password, user.getPassword());
        System.out.println("Password match result: " + passwordMatches);

        if (passwordMatches) {
            String token = jwtUtil.generateToken(user.getId(), user.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("email", user.getEmail());
            response.put("user", userData);
            return ResponseEntity.ok(response);
        }

        System.out.println("Password mismatch for user: " + email);
        return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }
}