package com.fcolucasvieira.smartdelivery.modules.customers.usecases;

import com.fcolucasvieira.smartdelivery.modules.customers.entity.CustomerEntity;
import com.fcolucasvieira.smartdelivery.modules.customers.repository.CustomerRepository;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.ViaCepDTO;
import com.fcolucasvieira.smartdelivery.integrations.zipcode.ViaCEPClient;
import com.fcolucasvieira.smartdelivery.modules.customers.dto.CreateCustomerRequest;
import com.fcolucasvieira.smartdelivery.modules.customers.mapper.CustomerMapper;
import com.fcolucasvieira.smartdelivery.modules.users.CreateUserUseCase;
import com.fcolucasvieira.smartdelivery.modules.users.Role;
import com.fcolucasvieira.smartdelivery.modules.users.UserEntity;
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

        // Cadastra usuário de role CUSTOMER (table users)
        UserEntity userEntity = this.createUserUseCase.execute(createCustomerRequest.email(), createCustomerRequest.password(), Role.CUSTOMER);

        // Seta o ID de usuário sobre a instancia customerEntity
        customerEntity.setUserId(userEntity.getId());

        this.customerRepository.save(customerEntity);
        return userEntity.getId();
    }
}
