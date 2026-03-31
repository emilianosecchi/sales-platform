package com.esecchi.userauth.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String roles = request.getHeader("X-User-Roles");
        String authHeader = request.getHeader("Authorization");

        // Primero se verifica el tráfico del gateway.
        if (StringUtils.hasText(userId) && StringUtils.hasText(roles)) {
            setAuthentication(userId, roles, request);
        }
        // Si no viene desde el gateway, se revisa si es tráfico interno desde otro microservicio.
        else if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtService.isTokenExpired(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            try {
                Claims claims = jwtService.getAllClaimsFromToken(token);
                String subject = claims.getSubject();
                String role = String.valueOf(claims.get("roles"));

                if (StringUtils.hasText(subject) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    setAuthentication(subject, role, request);
                }
            } catch (Exception e) {
                logger.error("No se pudo validar el JWT en la llamada interna.", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String principal, String roles, HttpServletRequest request) {
        List<SimpleGrantedAuthority> authorities = Arrays.stream(roles.split(","))
                .map(role -> new SimpleGrantedAuthority(role.trim().startsWith("ROLE_") ? role.trim() : "ROLE_" + role.trim()))
                .toList();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
