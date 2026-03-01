package com.fcolucasvieira.smartdelivery.modules.customers;

import com.fcolucasvieira.smartdelivery.ViaCepDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CreateCustomerUseCase {

    private CustomerRepository customerRepository;

    public CreateCustomerUseCase(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    // Substituir por práticas atuais (WebClient)
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public void execute(CustomerEntity customerEntity){
        // Url (API externa) para busca de informações via CEP
        String url = "https://viacep.com.br/ws/"+customerEntity.getZipCode()+"/json/";

        try {
            ViaCepDTO viaCepDTO = restTemplate.getForObject(url, ViaCepDTO.class);
            customerEntity.setAddress(viaCepDTO.logradouro());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Erro ao consultar CEP " + customerEntity.getZipCode());
        }

        // .ifPresent (Se presente, aplica a regra denominada sob as aspas)
        this.customerRepository.findByEmail(customerEntity.getEmail())
                .ifPresent(item -> {
                    throw new IllegalArgumentException("Email já existe");
                });


        this.customerRepository.save(customerEntity);
        System.out.println(customerEntity);

    }
}
