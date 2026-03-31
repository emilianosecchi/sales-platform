package com.esecchi.notification.client;

import com.esecchi.common.dto.userauth.request.ServiceLoginRequest;
import com.esecchi.common.dto.userauth.response.AuthenticationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-auth-service")
public interface UserAuthClient {

    @GetMapping("/api/v1/users/{id}/email")
    String getUserEmail(@PathVariable("id") Long id);

    @PostMapping("/api/v1/auth/services/login")
    AuthenticationResponse serviceLogin(@RequestBody ServiceLoginRequest request);

}
