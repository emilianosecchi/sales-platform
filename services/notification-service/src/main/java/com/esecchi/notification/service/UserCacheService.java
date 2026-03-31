package com.esecchi.notification.service;

import com.esecchi.notification.client.UserAuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private final String USER_EMAIL_PREFIX = "user:email:";
    private final UserAuthClient userAuthClient;

    public void setUserEmail(Long userId, String email) {
        redisTemplate.opsForValue().set(USER_EMAIL_PREFIX + userId, email);
    }

    public String getUserEmail(Long userId) {
        String userEmail = redisTemplate.opsForValue().get(USER_EMAIL_PREFIX + userId);
        if (StringUtils.hasText(userEmail))
            return userEmail;
        else {
            log.warn("Cache miss para el usuario con id: {}. Consultando servicio de usuarios.", userId);
            try {
                userEmail = userAuthClient.getUserEmail(userId);
                this.setUserEmail(userId, userEmail);
            } catch (Exception e) {
                log.error("No fue posible recuperar el email del usuario con id: {}.", userId);
                // FIXME: Cambiar el tipo de excepción lanzada
                throw new RuntimeException(e);
            }
        }
        return userEmail;
    }

}
