package com.esecchi.gateway.config;

import com.esecchi.gateway.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouterFunction<ServerResponse> GatewayMainRouter(AuthenticationFilter authFilter) {
        return userAuthPublicRoutes()
                .and(userAuthSecuredRoutes(authFilter));
    }

    private RouterFunction<ServerResponse> userAuthPublicRoutes() {
        return route("user-auth-service-public")
                .route(
                        path("/api/v1/auth/**"), http())
                .filter(
                        lb("user-auth-service"))
                .build();
    }

    private RouterFunction<ServerResponse> userAuthSecuredRoutes(AuthenticationFilter authFilter) {
        return route("user-auth-service-secured")
                .route(
                        path("/api/v1/users/**"), http())
                .filter(authFilter)
                .filter(
                        lb("user-auth-service"))
                .build();
    }
}
