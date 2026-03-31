package com.esecchi.userauth.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "internal.services")
public record InternalServicesConfig(Map<String, String> clients) {}