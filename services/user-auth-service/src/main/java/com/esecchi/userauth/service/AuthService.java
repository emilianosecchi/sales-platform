package com.esecchi.userauth.service;

import com.esecchi.userauth.model.User;
import com.esecchi.userauth.repository.UserRepository;
import com.esecchi.userauth.request.LoginRequest;
import com.esecchi.userauth.response.AuthenticationResponse;
import com.esecchi.userauth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponse authenticateUser(LoginRequest request) {
        // Si las credenciales no son válidas, lanza una excepción automáticamente
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow();
        String generatedToken = jwtService.generateToken(user);
        return new AuthenticationResponse(generatedToken);
    }
}
