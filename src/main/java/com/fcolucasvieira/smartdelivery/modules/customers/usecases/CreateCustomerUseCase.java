package com.fcolucasvieira.smartdelivery.modules.customers.usecases;

import com.fcolucasvieira.smartdelivery.core.exceptions.CustomerAlreadyExists;
import com.fcolucasvieira.smartdelivery.core.exceptions.ZipCodeNotFound;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerResponse;
import com.fcolucasvieira.smartdelivery.modules.customers.entity.CustomerEntity;
import com.fcolucasvieira.smartdelivery.modules.customers.repository.CustomerRepository;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.ViaCepResponse;
import com.fcolucasvieira.smartdelivery.integrations.zipcode.ViaCepClient;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerRequest;
import com.fcolucasvieira.smartdelivery.modules.customers.mapper.CustomerMapper;
import com.fcolucasvieira.smartdelivery.modules.users.usecases.CreateUserUseCase;
import com.fcolucasvieira.smartdelivery.modules.users.entity.enums.UserRole;
import com.fcolucasvieira.smartdelivery.modules.users.entity.UserEntity;
import com.fcolucasvieira.smartdelivery.modules.users.dto.CreateUserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerRepository repository;
    private final CreateUserUseCase createUserUseCase;
    private final ViaCepClient viaCepClient;

    @Transactional
    public CreateCustomerResponse execute(CreateCustomerRequest request){
        validateCustomer(request.phone());

        CustomerEntity customer = buildCustomer(request);

        enrichAddress(customer, request.zipCode());

        UserEntity user = createUserForCustomer(request);

        customer.setUserId(user.getId());

        this.repository.save(customer);

        return buildResponse(customer);
    }

    private void validateCustomer(String phone){
        this.repository.findByPhone(phone)
                .ifPresent(customer -> {
                    throw new CustomerAlreadyExists("Customer already exists with phone: " + phone);
                });
    }

    private CustomerEntity buildCustomer(CreateCustomerRequest request) {
        return CustomerMapper.toEntity(request);
    }

    private void enrichAddress(CustomerEntity customer, String zipCode) {
        try {
            ViaCepResponse viaCepResponse = this.viaCepClient.findZipCode(zipCode);

            if(Boolean.TRUE.equals(viaCepResponse.erro()))
                throw new ZipCodeNotFound("ZIP Code not found: " + zipCode);

            customer.setAddress(viaCepResponse.logradouro());
        } catch (Exception ex) {
            throw new ZipCodeNotFound("Error when searching for ZIP code: " + zipCode);
        }
    }

    private UserEntity createUserForCustomer(CreateCustomerRequest request) {
        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username(request.email())
                .password(request.password())
                .userRole(UserRole.CUSTOMER)
                .build();

        return this.createUserUseCase.execute(userRequest);
    }

    private CreateCustomerResponse buildResponse(CustomerEntity customer){
        return CustomerMapper.toResponse(customer);
    }
}
