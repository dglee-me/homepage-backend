package com.dglee.app.auth.filter;

import com.dglee.app.auth.provider.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        // Validation 성공 시 Authentication 정보를 가져와 Context에 넣어줍니다.
        if(StringUtils.hasText(token) && JwtTokenProvider.validateToken(token)) {
            SecurityContextHolder.getContext().setAuthentication(JwtTokenProvider.getAuthentication(token));
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(token) || token.startsWith(TOKEN_PREFIX)) {
            return null;
        }

        return token.substring(TOKEN_PREFIX.length());
    }
}