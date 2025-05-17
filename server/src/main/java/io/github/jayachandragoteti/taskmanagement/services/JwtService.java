package io.github.jayachandragoteti.taskmanagement.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.github.jayachandragoteti.taskmanagement.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "M4n6r9vAq1xBz7UjIwKxHjNp3sVg5eLq7YxCmZtVtAeFqWsRj9PbHw==";

    public JwtService() {
    }

    public String generateToken(User user) {
        Map<String, Object> claimes = new HashMap<>();
        claimes.put("username", user.getUsername());
        claimes.put("id", user.getId());
        return Jwts
                .builder()
                .claims(claimes)
                .subject(user.getUsername())
                .signWith(getKey())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .compact();
    }

    public SecretKey getKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
