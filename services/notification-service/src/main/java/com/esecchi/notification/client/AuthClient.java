package com.esecchi.notification.client;

import com.esecchi.common.dto.userauth.request.ServiceLoginRequest;
import com.esecchi.common.dto.userauth.response.AuthenticationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-auth-service", contextId = "authClient")
public interface AuthClient {

    @PostMapping("/api/v1/auth/services/login")
    AuthenticationResponse serviceLogin(@RequestBody ServiceLoginRequest request);

}