package com.esecchi.userauth.service;

import com.esecchi.userauth.exception.EmailAlreadyRegisteredException;
import com.esecchi.userauth.mapper.UserMapper;
import com.esecchi.userauth.model.User;
import com.esecchi.userauth.repository.UserRepository;
import com.esecchi.userauth.request.RegisterRequest;
import com.esecchi.userauth.response.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(RegisterRequest request) throws EmailAlreadyRegisteredException {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException(request.email());
        } else {
            User newUser = userMapper.toEntity(request);
            newUser.setPassword(passwordEncoder.encode(request.password()));
            userRepository.save(newUser);
            return userMapper.toResponse(newUser);
        }
    }
}
