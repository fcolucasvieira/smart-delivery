package com.fcolucasvieira.smartdelivery.modules.customers.usecases;

import com.fcolucasvieira.smartdelivery.modules.customers.entity.CustomerEntity;
import com.fcolucasvieira.smartdelivery.modules.customers.repository.CustomerRepository;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.ViaCepDTO;
import com.fcolucasvieira.smartdelivery.integrations.zipcode.ViaCEPClient;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerRequest;
import com.fcolucasvieira.smartdelivery.modules.customers.mapper.CustomerMapper;
import com.fcolucasvieira.smartdelivery.modules.users.usecases.CreateUserUseCase;
import com.fcolucasvieira.smartdelivery.modules.users.entity.enums.UserRole;
import com.fcolucasvieira.smartdelivery.modules.users.entity.UserEntity;
import com.fcolucasvieira.smartdelivery.modules.users.dto.CreateUserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CreateUserUseCase createUserUseCase;
    private final ViaCEPClient viaCEPClient;

    // @Transactional - Metodo é efetivado caso todas persistências do banco de dados forem concretizadas
    @Transactional
    public UUID execute(CreateCustomerRequest createCustomerRequest){
        // Instância de customerEntity (set de dados iniciais via CustomerMapper)
        CustomerEntity customerEntity = CustomerMapper.toEntity(createCustomerRequest);

        // Instância de ViaCepDTO (armazena CEP e logradouro através de rota (API externa))
        // Seta address através de viaCepDTO
        try {
            ViaCepDTO viaCepDTO = this.viaCEPClient.findZipCode(createCustomerRequest.zipCode());
            customerEntity.setAddress(viaCepDTO.logradouro());
        } catch (Exception ex){
            throw new IllegalArgumentException("Erro ao consultar CEP " + createCustomerRequest.zipCode());
        }

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username(createCustomerRequest.email())
                .password(createCustomerRequest.password())
                .userRole(UserRole.CUSTOMER)
                .build();

        // Cadastra usuário de role CUSTOMER (table users)
        UserEntity userEntity = this.createUserUseCase.execute(userRequest);

        // Seta o ID de usuário sobre a instancia customerEntity
        customerEntity.setUserId(userEntity.getId());

        this.customerRepository.save(customerEntity);
        return userEntity.getId();
    }
}
