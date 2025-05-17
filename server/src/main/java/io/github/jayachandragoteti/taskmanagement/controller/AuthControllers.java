package io.github.jayachandragoteti.taskmanagement.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.jayachandragoteti.taskmanagement.models.User;
import io.github.jayachandragoteti.taskmanagement.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    final UserService userService;

    public AuthControllers(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, Object> registerUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody User user) {
        Map<String, Object> loginResponse = new HashMap<>();
        String token = userService.verifyUser(user);
        if (token == "" || token == null) {
            loginResponse.put("error", "Invalid username or password");
        } else {
            loginResponse.put("token", token);
        }
        return loginResponse;
    }
}
