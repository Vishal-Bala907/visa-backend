package com.visa.services.imple;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
//    @Value("${jwt.secret.key}")
//    private String SECRET_KEY;
    private String SECRET_KEY = "J5yH01iaIj6WJ/ZWZCG9Hfw/EMGdOx5aWs8G2BhIDGg=";

//    @Value("${jwt.token.expiry}")
    private long tokenExpiryDuration = 604800000;

    public String generateToken(String mobileNumber) {
        return Jwts.builder()
                .setSubject(mobileNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiryDuration)) // Configurable expiration time
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractMobileNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Key getSignKey() {
        byte[] keybytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keybytes);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid or expired JWT token", e);
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String mobile = extractMobileNumber(token);
        return (mobile.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
