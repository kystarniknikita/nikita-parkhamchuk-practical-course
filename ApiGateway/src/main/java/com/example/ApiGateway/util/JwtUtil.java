//package com.example.ApiGateway.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//import java.util.function.Function;
//
//@Component
//public class JwtUtil {
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Value("${jwt.expiration}")
//    private long jwtExpiration;
//
//    private SecretKey getSigningKey() {
//        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//    }
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    public String generateToken(String username, String email, Long userId) {
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("email", email)
//                .claim("userId", userId)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public Boolean validateToken(String token, String username) {
//        final String extractedUsername = extractUsername(token);
//        return (extractedUsername.equals(username) && !isTokenExpired(token));
//    }
//
//    public Boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token);
//            return !isTokenExpired(token);
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public Long extractUserId(String token) {
//        Claims claims = extractAllClaims(token);
//        return claims.get("userId", Long.class);
//    }
//
//    public String extractEmail(String token) {
//        Claims claims = extractAllClaims(token);
//        return claims.get("email", String.class);
//    }
//}