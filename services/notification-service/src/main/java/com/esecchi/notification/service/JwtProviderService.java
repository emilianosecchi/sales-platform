package com.esecchi.notification.service;

import com.esecchi.common.dto.userauth.request.ServiceLoginRequest;
import com.esecchi.common.dto.userauth.response.AuthenticationResponse;
import com.esecchi.notification.client.UserAuthClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtProviderService {

    private final UserAuthClient userAuthClient;
    private String token;

    @Value("${jwt.expiration}")
    private Long jwtDurationMilliseconds;
    private LocalDateTime expirationTime;

    @Value("${spring.application.name}")
    private String clientId;
    @Value("${internal.auth.secret}")
    private String clientSecret;

    /**
     * Se ejecuta al iniciar el microservicio para asegurar que las
     * primeras peticiones ya tengan un token jwt disponible.
     */
    @PostConstruct
    public void init() {
        log.info("Inicializando JwtProviderService...");
        refreshToken();
    }

    /**
     * Punto de entrada para el Jwt Request Interceptor.
     * Devuelve el token en memoria de forma rápida.
     */
    public String getAccessToken() {
        if (shouldRefresh()) {
            synchronized (this) {
                if (shouldRefresh()) {
                    refreshToken();
                }
            }
        }
        return token;
    }

    /**
     * Tarea programada que corre cada 15 minutos.
     * Si detecta que al token le quedan menos de 10 minutos de vida, lo renueva.
     */
    @Scheduled(fixedRate = 900000)
    public void scheduledTokenRefresh() {
        log.debug("Validando la expiración del token jwt.");
        if (shouldRefresh()) {
            refreshToken();
        }
    }

    private boolean shouldRefresh() {
        // Renovar si no hay token o si vence en menos de 10 minutos
        return token == null ||
                expirationTime == null ||
                LocalDateTime.now().isAfter(expirationTime.minusMinutes(10));
    }

    private void refreshToken() {
        try {
            log.info("Solicitando token de servicio para el cliente: {}", this.clientId);

            ServiceLoginRequest request = new ServiceLoginRequest(
                    this.clientId,
                    this.clientSecret
            );

            AuthenticationResponse response = userAuthClient.serviceLogin(request);

            this.token = response.accessToken();
            this.expirationTime = LocalDateTime.now().plusHours(
                    Duration.ofMillis(jwtDurationMilliseconds).toHours()
            );

            log.info("Token de servicio obtenido exitosamente. Vence a las: {}", expirationTime);

        } catch (Exception e) {
            log.error("Error crítico al intentar autenticar el microservicio: {}", e.getMessage());
        }
    }

}
