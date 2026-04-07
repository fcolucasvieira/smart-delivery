package com.fcolucasvieira.smartdelivery.modules.users.usecases;

import com.fcolucasvieira.smartdelivery.core.exceptions.UserAlreadyExists;
import com.fcolucasvieira.smartdelivery.modules.users.dto.CreateUserRequest;
import com.fcolucasvieira.smartdelivery.modules.users.entity.UserEntity;
import com.fcolucasvieira.smartdelivery.modules.users.entity.enums.UserRole;
import com.fcolucasvieira.smartdelivery.modules.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Test
    @DisplayName("Should create user successfully when username does not exists")
    void Success() {
        // Arrange
        String username = "test-email@gmail.com";
        String passwordEncode = "encode-password";
        CreateUserRequest request = CreateUserRequest.builder()
                .username(username)
                .password("test-password")
                .userRole(UserRole.CUSTOMER)
                .build();

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncode)
                .userRole(UserRole.CUSTOMER)
                .build();

        when(this.repository.findByUsername(username))
                .thenReturn(Optional.empty());
        when(encoder.encode(anyString()))
                .thenReturn(passwordEncode);
        when(this.repository.save(any(UserEntity.class)))
                .thenReturn(user);

        // Act
        UserEntity result = useCase.execute(request);

        // Assert
        assertEquals(username, result.getUsername());
        assertEquals(passwordEncode, result.getPassword());
        assertEquals(UserRole.CUSTOMER, result.getUserRole());
        verify(this.repository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void UsernameAlreadyExists() {
        // Arrange
        String username = "test-email@gmail.com";

        CreateUserRequest request = CreateUserRequest.builder()
                .username(username)
                .password("test-password")
                .userRole(UserRole.CUSTOMER)
                .build();

        when(this.repository.findByUsername(username))
                .thenReturn(Optional.of(new UserEntity()));

        // Act & Assert
        assertThrows(UserAlreadyExists.class, () -> this.useCase.execute(request));
        verify(this.repository, never()).save(any(UserEntity.class));
    }
}