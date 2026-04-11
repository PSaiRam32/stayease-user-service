package com.stayease.user_service.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET = "stayease-super-secret-key-for-jwt-authentication-2026-secure-key";

    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/refresh-token",
            "/users/auth-internal"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (PUBLIC_PATHS.stream().anyMatch(path::contains)) {
            filterChain.doFilter(request, response);
            return;
        }
        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-User-Role");
        if (userId != null && role != null) {
            setAuthentication(userId, role);
        } else {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    Claims claims = Jwts.parser()
                            .setSigningKey(SECRET.getBytes())
                            .parseClaimsJws(token)
                            .getBody();
                    String extractedUserId = claims.getSubject();
                    String extractedRole = claims.get("role", String.class);
                    setAuthentication(extractedUserId, extractedRole);
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
    private void setAuthentication(String userId, String role) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
