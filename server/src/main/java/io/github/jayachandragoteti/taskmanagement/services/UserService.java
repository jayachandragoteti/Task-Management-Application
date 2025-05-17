package io.github.jayachandragoteti.taskmanagement.services;

import io.github.jayachandragoteti.taskmanagement.exceptions.CustomErrorException;
import io.github.jayachandragoteti.taskmanagement.models.User;
import io.github.jayachandragoteti.taskmanagement.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    // Create or Save a new user
    public Map<String, Object> saveUser(User user) {
        if (existsByUsername(user.getUsername())) {
            // Use error code 005 => conflict
            throw new CustomErrorException("009");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("username", user.getUsername());
        newUser.put("id", user.getId());
        return newUser;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id)
                .or(() -> {
                    // Use error code 003 => not_found
                    throw new CustomErrorException("007");
                });
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            // Update username if provided and not empty
            if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
                existingUser.setUsername(updatedUser.getUsername());
            }
            // Update role if provided and not empty
            if (updatedUser.getRole() != null && !updatedUser.getRole().isEmpty()) {
                existingUser.setRole(updatedUser.getRole());
            }
            // Update password if provided and not empty
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new CustomErrorException("007"));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomErrorException("007");
        }
        userRepository.deleteById(id);
    }

    public String verifyUser(User user) {
        if (isValidUser(user)) {
            return jwtService.generateToken(user);
        } else {
            // Use error code 001 => unauthorized
            throw new CustomErrorException("001");
        }
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

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
