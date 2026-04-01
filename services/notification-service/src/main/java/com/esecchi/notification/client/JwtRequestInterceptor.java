package com.esecchi.notification.client;

import com.esecchi.notification.service.JwtProviderService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtRequestInterceptor implements RequestInterceptor {

    private final JwtProviderService jwtProviderService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = jwtProviderService.getAccessToken();
        requestTemplate.header("Authorization", "Bearer " + token);
    }

}