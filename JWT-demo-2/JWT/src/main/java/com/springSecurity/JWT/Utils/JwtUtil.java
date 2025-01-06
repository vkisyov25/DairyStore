package com.springSecurity.JWT.Utils;

import io.jsonwebtoken.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
@Component
public class JwtUtil {
    private static final String SECRET_KEY = "2e73012638c9f832c35c3f45e3b2e257b9204f85ea13a1a0bdbf2542e45bc6f3";
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                //.claim("roles", userDetails.getAuthorities())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 час
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Метод за валидиране на токен
    public boolean validateToken(String token) {
        String username = extractUsername(token);
        return (username != null && !isTokenExpired(token));
    }

    // Проверка за изтекъл токен
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)  // Тайният ключ
                .parseClaimsJws(token)
                .getBody();

        List<String> roles = claims.get("roles", List.class);  // Вземи ролите като списък
        return roles != null && !roles.isEmpty() ? roles.get(0) : null;  // Върни първата роля, ако съществува
    }
    public Collection<? extends GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = extractAllClaims(token);
        // Извличаме ролите като Object и ги конвертираме в List<String>
        Object rolesObj = claims.get("roles", Object.class);
        List<String> roles = null;
        if (rolesObj instanceof List) {
            roles = (List<String>) rolesObj;
        } else if (rolesObj instanceof String) {
            // Ако ролите са запазени като низ, разделяме ги
            roles = Arrays.asList(((String) rolesObj).split(","));
        }

        // Преобразуваме списъка от роли в GrantedAuthority обекти
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}

