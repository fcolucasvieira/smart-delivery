package com.fcolucasvieira.smartdelivery.modules.customers.usecases;

import com.fcolucasvieira.smartdelivery.core.exceptions.CustomerAlreadyExists;
import com.fcolucasvieira.smartdelivery.core.exceptions.ZipCodeNotFound;
import com.fcolucasvieira.smartdelivery.integrations.zipcode.ViaCepClient;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerRequest;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerResponse;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.ViaCepResponse;
import com.fcolucasvieira.smartdelivery.modules.customers.entity.CustomerEntity;
import com.fcolucasvieira.smartdelivery.modules.customers.repository.CustomerRepository;
import com.fcolucasvieira.smartdelivery.modules.users.dto.CreateUserRequest;
import com.fcolucasvieira.smartdelivery.modules.users.entity.UserEntity;
import com.fcolucasvieira.smartdelivery.modules.users.entity.enums.UserRole;
import com.fcolucasvieira.smartdelivery.modules.users.usecases.CreateUserUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {

    @Mock
    CustomerRepository repository;

    @Mock
    CreateUserUseCase createUserUseCase;

    @Mock
    ViaCepClient viaCepClient;

    @InjectMocks
    CreateCustomerUseCase useCase;

    @Test
    @DisplayName("Should create customer successfully")
    void Success() {
        // Arrange
        String name = "Customer test";
        String phone = "88999999999";
        String email = "test-email@gmail.com";
        String zipCode = "01001000";
        String password = "123456";
        String address = "Praça da Sé";

        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .name(name)
                .phone(phone)
                .email(email)
                .zipCode(zipCode)
                .password(password)
                .build();

        UserEntity user = UserEntity.builder()
                .id(UUID.randomUUID())
                .username(email)
                .password(password)
                .userRole(UserRole.CUSTOMER)
                .build();

        CustomerEntity customer = CustomerEntity.builder()
                .name(name)
                .phone(phone)
                .email(email)
                .address(address)
                .zipCode(zipCode)
                .build();

        when(this.repository.findByPhone(phone))
                .thenReturn(Optional.empty());
        when(this.viaCepClient.findZipCode(zipCode))
                .thenReturn(new ViaCepResponse("01001-000", address, null));
        when(this.repository.save(any(CustomerEntity.class)))
                .thenReturn(customer);
        when(this.createUserUseCase.execute(any(CreateUserRequest.class)))
                .thenReturn(user);

        // Act
        CreateCustomerResponse result = useCase.execute(request);

        // Assert
        assertEquals(name, result.name());
        assertEquals(phone, result.phone());
        assertEquals(email, result.email());
    }

    @Test
    @DisplayName("Should throw CustomerAlreadyExists when phone already exists")
    void PhoneAlreadyExists() {
        // Arrange
        String phone = "88999999999";

        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .name("Customer test")
                .phone(phone)
                .email("test-email@gmail.com")
                .zipCode("01001000")
                .password("123456")
                .build();

        when(this.repository.findByPhone(phone))
                .thenReturn(Optional.of(new CustomerEntity()));

        // Act & Assert
        assertThrows(CustomerAlreadyExists.class, () -> useCase.execute(request));
    }

    @Test
    @DisplayName("Should throw ZipCodeNotFound when CEP is invalid")
    void InvalidCep() {
        // Arrange
        String zipCode = "0000000";

        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .name("Customer test")
                .phone("88999999999")
                .email("test-email@gmail.com")
                .zipCode(zipCode)
                .password("123456")
                .build();

        when(this.viaCepClient.findZipCode(zipCode))
                .thenThrow(new RuntimeException("Time out"));

        // Act & Asset
        assertThrows(ZipCodeNotFound.class, () -> useCase.execute(request));
    }

    @Test
    @DisplayName("Should throw ZipCodeNotFound when integration fails unexpectedly")
    void IntegrationError() {
        // Arrange
        String zipCode = "01001000";

        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .name("Integration error")
                .phone("88999999999")
                .email("error@gmail.com")
                .zipCode(zipCode)
                .password("123456")
                .build();

        when(viaCepClient.findZipCode(zipCode))
                .thenThrow(new RuntimeException("Timeout"));

        // Act & Assert
        assertThrows(ZipCodeNotFound.class, () -> useCase.execute(request));
    }

}