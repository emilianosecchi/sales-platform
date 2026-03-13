package com.esecchi.gateway.filter;

import com.esecchi.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final JwtUtil jwtUtil;

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        Optional<String> authHeader = request.headers().header(HttpHeaders.AUTHORIZATION).stream().findFirst();
        if (authHeader.isEmpty() || !authHeader.get().startsWith("Bearer ")) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.get().substring(7);
        try {
            // Se valida el token
            if (jwtUtil.isTokenExpired(token)) {
                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
            }
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            // Se inyecta la identidad en la request para los microservicios
            ServerRequest modifiedRequest = ServerRequest.from(request)
                    .header("X-User-Id", claims.get("userId", String.class))
                    .header("X-User-Roles", claims.get("roles", String.class))
                    .build();

            return next.handle(modifiedRequest);

        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
