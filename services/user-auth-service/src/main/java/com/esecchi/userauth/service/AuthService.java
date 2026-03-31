package com.esecchi.userauth.service;

import com.esecchi.userauth.model.User;
import com.esecchi.userauth.repository.UserRepository;
import com.esecchi.userauth.request.LoginRequest;
import com.esecchi.common.dto.userauth.request.ServiceLoginRequest;
import com.esecchi.common.dto.userauth.response.AuthenticationResponse;
import com.esecchi.userauth.security.InternalServicesConfig;
import com.esecchi.userauth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final InternalServicesConfig servicesConfig;

    public AuthenticationResponse authenticateUser(LoginRequest request) {
        // Si las credenciales no son válidas, lanza una excepción automáticamente
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow();
        String generatedToken = jwtService.generateUserToken(user);
        return new AuthenticationResponse(generatedToken);
    }

    public AuthenticationResponse authenticateService(ServiceLoginRequest request) {
        String service = servicesConfig.clients().get(request.clientId());
        if (hasText(service) && service.equals(request.clientSecret())) {
            return new AuthenticationResponse(jwtService.generateServiceToken(request.clientId()));
        } else
            throw new BadCredentialsException("Las credenciales del servicio no son válidas.");
    }

}
