package com.esecchi.userauth.service;

import com.esecchi.userauth.exception.EmailAlreadyRegisteredException;
import com.esecchi.userauth.exception.UserNotFoundException;
import com.esecchi.userauth.mapper.UserMapper;
import com.esecchi.userauth.model.Role;
import com.esecchi.userauth.model.User;
import com.esecchi.userauth.repository.UserRepository;
import com.esecchi.userauth.request.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    public void setUp() {
        existingUser = User.builder()
                .registrationDate(LocalDate.now())
                .id(1L)
                .role(Role.ADMIN)
                .email("mock_user@test.com")
                .firstName("mock_name")
                .lastName("mock_lastname")
                .password("old_password")
                .enabled(true)
                .build();
    }

    @Test
    void updateUserById_UserNotFound_ThrowsException() {
        // Arrange
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        UserUpdateRequest request = new UserUpdateRequest("Lucas", null, null, null);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUserById(userId, request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserById_EmailAlreadyExists_ThrowsException() {
        // Arrange
        String newEmail = "existing@test.com";
        Long userId = 1L;
        UserUpdateRequest request = new UserUpdateRequest(null, null, newEmail, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyRegisteredException.class, () -> userService.updateUserById(userId, request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserById_PartialUpdate_Success() {
        // Arrange
        Long userId = 1L;
        String newEmail = "new@test.com";
        String newFirstName = "Emiliano";
        LocalDate originalRegistrationDate = existingUser.getRegistrationDate();
        UserUpdateRequest request = new UserUpdateRequest(newFirstName, null, newEmail, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        userService.updateUserById(userId, request);

        // Assert
        verify(userMapper).updateEntityFromRequest(request, existingUser);
        verify(passwordEncoder, never()).encode(anyString());
        assertEquals(newEmail, existingUser.getEmail());
        assertEquals(originalRegistrationDate, existingUser.getRegistrationDate());
        assertEquals(newFirstName, existingUser.getFirstName());
        assertNotEquals(null, existingUser.getLastName());
        assertNotEquals(null, existingUser.getPassword());
    }
}
