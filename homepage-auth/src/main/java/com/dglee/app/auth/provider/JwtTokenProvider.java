package com.dglee.app.auth.provider;

import com.dglee.app.user.entity.User;
import com.dglee.app.user.enums.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenProvider {

    private static final String CLAIM_AUTH = "auth";

    @Value("${auth.jwt.secret")
    private static String SECRET_KEY;

    public static String generateToken(Authentication authentication) {
        
        // 권한
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject((String) authentication.getPrincipal())
                .claim(CLAIM_AUTH, authorities)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))   // 하루동안 인증처리
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8))).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid Signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid Token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired Token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported Token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }

        return false;
    }

    public static Authentication getAuthentication(String token) {
        Claims claims = parseClaim(token);
        if(claims.get(CLAIM_AUTH) == null) {
            throw new IllegalArgumentException("권한 정보가 없습니다.");
        }

        // Claim으로부터 권한 정보 GET
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(CLAIM_AUTH).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());


        UserDetails principal = User.builder()
                                    .email((String) claims.get("subject"))
                                    .authorities(authorities)
                                    .build();

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private static Claims parseClaim(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8))).build().parseClaimsJws(accessToken).getBody();
    }
}