package io.github.jayachandragoteti.taskmanagement.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.jayachandragoteti.taskmanagement.services.CustomUserDetailsService;
import io.github.jayachandragoteti.taskmanagement.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    final JwtService jwtService;

    @Autowired
    private ApplicationContext context;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Autharization");
        String username = null;
        String token = null;

        if (authHeader != null && authHeader.contains("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.getUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetailsService userDetailsService = context.getBean(CustomUserDetailsService.class);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            
        }
        filterChain.doFilter(request, response);
    }

}
