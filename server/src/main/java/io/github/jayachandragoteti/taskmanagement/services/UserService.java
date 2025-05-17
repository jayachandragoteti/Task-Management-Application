package io.github.jayachandragoteti.taskmanagement.services;

import io.github.jayachandragoteti.taskmanagement.models.User;
import io.github.jayachandragoteti.taskmanagement.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // ✅ Create or Save a new user
    public Map<String,Object> saveUser(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        Map<String,Object> newUser  = new HashMap<>();
        newUser.put("username", user.getUsername());
        newUser.put("id", user.getId());
        return newUser;
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user (excluding password by default)
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setRole(updatedUser.getRole());
            // Only update password if provided
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            return userRepository.save(existingUser);
        });
    }

    // Delete user by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Get user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Check if a username already exists
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isValidUser(User user) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword());
            Authentication auth = authenticationManager.authenticate(authentication);
            return auth.isAuthenticated();
        } catch (AuthenticationException ex) {
            return false;
        }
    }

    public String verifyUser(User user) {
        if (isValidUser(user)) {
            return jwtService.generateToken(user);
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
