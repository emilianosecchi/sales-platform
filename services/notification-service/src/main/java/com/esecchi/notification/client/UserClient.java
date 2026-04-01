package com.esecchi.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-auth-service",
        contextId = "userClient",
        configuration = {JwtRequestInterceptor.class})
public interface UserClient {

    @GetMapping("/api/v1/users/{id}/email")
    String getUserEmail(@PathVariable("id") Long id);

}