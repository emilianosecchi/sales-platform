package com.esecchi.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El "name" debe coincidir con el nombre del microservicio en Eureka/Config
@FeignClient(name = "user-auth-service")
public interface UserClient {

    @GetMapping("/api/v1/users/{id}/email")
    String getUserEmail(@PathVariable("id") Long id);

}