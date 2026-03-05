package com.fcolucasvieira.smartdelivery.modules.customers;

import com.fcolucasvieira.smartdelivery.ViaCepDTO;
import com.fcolucasvieira.smartdelivery.modules.users.CreateUserUseCase;
import com.fcolucasvieira.smartdelivery.modules.users.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CreateCustomerUseCase {

    private CustomerRepository customerRepository;
    private CreateUserUseCase createUserUseCase;

    public CreateCustomerUseCase(CustomerRepository customerRepository, CreateUserUseCase createUserUseCase){
        this.customerRepository = customerRepository;
        this.createUserUseCase = createUserUseCase;
    }

    // Substituir por práticas atuais (WebClient)
    private final RestTemplate restTemplate = new RestTemplate();

    // @Transactional - O metodo só implementa as persistências do banco de dados caso todas sejam efetivadas
    // Neste caso, mesmo que save User aconteça antes do fim do metodo, ele só será efetivado após o save Customer
    @Transactional
    public void execute(CreateCustomerRequest createCustomerRequest){
        // Url (API externa) para busca de informações via CEP
        String url = "https://viacep.com.br/ws/"+createCustomerRequest.zipCode()+"/json/";

        CustomerEntity customerEntity = new CustomerEntity();

        try {
            ViaCepDTO viaCepDTO = restTemplate.getForObject(url, ViaCepDTO.class);
            customerEntity.setAddress(viaCepDTO.logradouro());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Erro ao consultar CEP " + createCustomerRequest.zipCode());
        }

            this.createUserUseCase.execute(createCustomerRequest.email(), createCustomerRequest.password(), Role.CUSTOMER);

        customerEntity.setName(createCustomerRequest.name());
        customerEntity.setPhone(createCustomerRequest.phone());
        customerEntity.setEmail(createCustomerRequest.email());
        customerEntity.setZipCode(createCustomerRequest.zipCode());

        // .ifPresent (Se presente, aplica a regra denominada sob as aspas)
        this.customerRepository.findByEmail(customerEntity.getEmail())
                .ifPresent(item -> {
                    throw new IllegalArgumentException("Email já existe");
                });

        this.customerRepository.save(customerEntity);
        System.out.println(customerEntity);
    }
}
