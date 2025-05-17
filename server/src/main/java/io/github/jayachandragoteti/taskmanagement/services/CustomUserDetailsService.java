package io.github.jayachandragoteti.taskmanagement.services;

import org.springframework.stereotype.Service;

import io.github.jayachandragoteti.taskmanagement.models.CustomUserDetails;
import io.github.jayachandragoteti.taskmanagement.models.User;
import io.github.jayachandragoteti.taskmanagement.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomUserDetails(user);
    }

}