package com.esecchi.userauth.service;

import com.esecchi.common.event.user.UserMailUpdatedEvent;
import com.esecchi.userauth.exception.EmailAlreadyRegisteredException;
import com.esecchi.userauth.exception.UserNotFoundException;
import com.esecchi.userauth.mapper.UserMapper;
import com.esecchi.userauth.messaging.UserEventProducer;
import com.esecchi.userauth.model.User;
import com.esecchi.userauth.repository.UserRepository;
import com.esecchi.userauth.request.RegisterRequest;
import com.esecchi.userauth.request.UserUpdateRequest;
import com.esecchi.userauth.response.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;

    public UserResponseDTO createUser(RegisterRequest request) throws EmailAlreadyRegisteredException {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException(request.email());
        } else {
            User newUser = userMapper.toEntity(request);
            newUser.setPassword(passwordEncoder.encode(request.password()));
            userRepository.save(newUser);
            userEventProducer.publishUserCreatedEvent(
                    userMapper.toCreatedEvent(newUser)
            );
            return userMapper.toResponse(newUser);
        }
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toResponse).toList();
    }

    public UserResponseDTO getUserById(Long userId) {
        return userMapper.toResponse(
            this.findUserEntityById(userId)
        );
    }

    public UserResponseDTO updateUserById(Long userId, UserUpdateRequest request) {
        boolean mailChanged = false;
        User user = this.findUserEntityById(userId);
        if (StringUtils.hasText(request.email())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new EmailAlreadyRegisteredException(request.email());
            } else {
                user.setEmail(request.email());
                mailChanged = true;
            }
        }
        if (StringUtils.hasText(request.password()))
            user.setPassword(passwordEncoder.encode(request.password()));
        userMapper.updateEntityFromRequest(request, user);
        userRepository.save(user);
        if (mailChanged) {
            userEventProducer.publishUserEmailUpdatedEvent(
                    new UserMailUpdatedEvent(user.getId(), request.email())
            );
        }
        return userMapper.toResponse(user);
    }

    public String getUserEmailById(Long userId) {
        User user = this.findUserEntityById(userId);
        return user.getEmail();
    }

    private User findUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

}