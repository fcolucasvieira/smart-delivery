package com.fcolucasvieira.smartdelivery.modules.customers;

import com.fcolucasvieira.smartdelivery.ViaCepDTO;
import com.fcolucasvieira.smartdelivery.integrations.zipcode.ViaCEPClient;
import com.fcolucasvieira.smartdelivery.modules.users.CreateUserUseCase;
import com.fcolucasvieira.smartdelivery.modules.users.Role;
import com.fcolucasvieira.smartdelivery.modules.users.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CreateCustomerUseCase {

    private CustomerRepository customerRepository;
    private CreateUserUseCase createUserUseCase;
    private ViaCEPClient viaCEPClient;

    public CreateCustomerUseCase(CustomerRepository customerRepository, CreateUserUseCase createUserUseCase, ViaCEPClient viaCEPClient) {
        this.customerRepository = customerRepository;
        this.createUserUseCase = createUserUseCase;
        this.viaCEPClient = viaCEPClient;
    }

    private final RestTemplate restTemplate = new RestTemplate();

    // @Transactional - O metodo só implementa as persistências do banco de dados caso todas sejam efetivadas
    // Neste caso, mesmo que save User aconteça antes do fim do metodo, ele só será efetivado após o save Customer
    @Transactional
    public void execute(CreateCustomerRequest createCustomerRequest){
        // Uso de OpenFeign (Spring Cloud)
        // Através da rota (API externa), gera instância ViaCepDTO e guarda CEP e logradouro
        ViaCepDTO viaCepDTO = this.viaCEPClient.findZipCode(createCustomerRequest.zipCode());

        CustomerEntity customerEntity = new CustomerEntity();

        // Insere sobre a instância customerEntity o address (logradouro de ViaCepDTO)
        try {
            customerEntity.setAddress(viaCepDTO.logradouro());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Erro ao consultar CEP " + createCustomerRequest.zipCode());
        }

        // Cadastra usuário de role CUSTOMER (table users)
        UserEntity userEntity = this.createUserUseCase.execute(createCustomerRequest.email(), createCustomerRequest.password(), Role.CUSTOMER);

        // Seta dados sobre a instância customerEntity através de createCustomerRequest e userEntity
        customerEntity.setName(createCustomerRequest.name());
        customerEntity.setPhone(createCustomerRequest.phone());
        customerEntity.setEmail(createCustomerRequest.email());
        customerEntity.setZipCode(createCustomerRequest.zipCode());
        customerEntity.setUserId(userEntity.getId());

        // .ifPresent (Se presente, aplica a regra denominada sob as aspas)
        // Neste caso, se existir um customer com este email setado, aplica exceção
        this.customerRepository.findByEmail(customerEntity.getEmail())
                .ifPresent(item -> {
                    throw new IllegalArgumentException("Email já existe");
                });

        this.customerRepository.save(customerEntity);
        System.out.println(customerEntity);
    }
}
