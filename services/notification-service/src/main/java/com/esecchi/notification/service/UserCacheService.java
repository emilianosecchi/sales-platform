package com.esecchi.notification.service;

import com.esecchi.notification.client.UserClient;
import com.esecchi.notification.exception.ServiceUnavailableException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private final String USER_EMAIL_PREFIX = "user:email:";
    private final UserClient userClient;

    public void setUserEmail(Long userId, String email) {
        redisTemplate.opsForValue().set(USER_EMAIL_PREFIX + userId, email);
    }

    public Optional<String> getUserEmail(Long userId) {
        String userEmail = redisTemplate.opsForValue().get(USER_EMAIL_PREFIX + userId);

        if (StringUtils.hasText(userEmail)) {
            return Optional.of(userEmail);
        }

        log.warn("Cache miss para el usuario con id: {}. Consultando servicio de usuarios.", userId);

        try {
            userEmail = userClient.getUserEmail(userId);

            if (StringUtils.hasText(userEmail)) {
                this.setUserEmail(userId, userEmail);
            }

            return Optional.ofNullable(userEmail);

        } catch (FeignException.NotFound e) {
            log.error("No se pudo encontrar el usuario con id {}", userId);
            return Optional.empty();

        } catch (Exception e) {
            // Error de red o servicio caído: lanzamos excepción para que Kafka reintente.
            log.error("Error de comunicación con user-auth-service para el id {}: {}", userId, e.getMessage());
            throw new ServiceUnavailableException("Servicio de usuarios no disponible temporalmente");
        }
    }

}
