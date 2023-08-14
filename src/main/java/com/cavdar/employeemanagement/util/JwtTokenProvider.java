package com.cavdar.employeemanagement.util;

import com.cavdar.employeemanagement.domain.model.Authority;
import com.cavdar.employeemanagement.domain.model.Employee;
import com.cavdar.employeemanagement.domain.model.User;
import com.cavdar.employeemanagement.domain.service.EmployeeService;
import com.cavdar.employeemanagement.util.exception.EmployeeNotFoundException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private long validityTime;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private EmployeeService employeeService;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        return generateTokenFromUsername(userPrincipal.getUsername());

//        Date now = new Date();
//        Date expirationDate = new Date(now.getTime() + validityTime);
//
//        Long employeeId = null;
//        try {
//            employeeId = employeeService.getEmployeeByUserId(userPrincipal.getId()).getId();
//        } catch (EmployeeNotFoundException ignored) {
//        }
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("id", userPrincipal.getId());
//        claims.put("authorities", userPrincipal.getAuthorities().stream().map(Authority::getAuthority).toList());
//        claims.put("employee_id", employeeId);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(userPrincipal.getUsername())
//                .setIssuedAt(now)
//                .setExpiration(expirationDate)
//                .signWith(key(), SignatureAlgorithm.HS256)
//                .compact();
    }

    public String generateTokenFromUsername(String username) {
        User user = (User) userDetailsService.loadUserByUsername(username);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + validityTime);

        Long employeeId = null;
        try {
            employeeId = employeeService.getEmployeeByUsername(username).getId();
        } catch (EmployeeNotFoundException ignored) {
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("authorities", user.getAuthorities().stream().map(Authority::getAuthority).toList());
        claims.put("employee_id", employeeId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            logger.error("Error while validating JWT: {}", e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

}
