package com.esecchi.userauth.service;

import com.esecchi.userauth.exception.EmailAlreadyRegisteredException;
import com.esecchi.userauth.mapper.UserMapper;
import com.esecchi.userauth.model.User;
import com.esecchi.userauth.repository.UserRepository;
import com.esecchi.userauth.request.LoginRequest;
import com.esecchi.userauth.request.RegisterRequest;
import com.esecchi.userauth.response.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(RegisterRequest request) throws EmailAlreadyRegisteredException {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException(request.email());
        } else {
            User newUser = userMapper.toEntity(request);
            newUser.setPassword(request.password());
            userRepository.save(newUser);
            return userMapper.toResponse(newUser);
        }
    }

    public void authenticateUser(LoginRequest request) {}
}
