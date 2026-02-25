package com.example.knowledgeplatform.service;

import com.example.knowledgeplatform.entity.User;
import com.example.knowledgeplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(String username, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Encoding password for " + email + ". Raw length: " + password.length()
                + ", Encoded length: " + encodedPassword.length());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        if (encodedPassword == null) {
            System.out.println("CheckPassword failed: encodedPassword is null");
            return false;
        }
        System.out.println("Checking password. Raw length: " + rawPassword.length() + ", Encoded length: "
                + encodedPassword.length());
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("Password match: " + matches);
        return matches;
    }
}