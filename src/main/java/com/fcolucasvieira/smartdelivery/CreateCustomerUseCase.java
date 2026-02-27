package com.fcolucasvieira.smartdelivery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CreateCustomerUseCase {

    @PersistenceContext
    private EntityManager em;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public void execute(CustomerEntity customerEntity){
        String url = "https://viacep.com.br/ws/"+customerEntity.getZipCode()+"/json/";

        restTemplate.getForObject(url, String.class);

        String jpql = "SELECT count(c) FROM CustomerEntity c WHERE c.email = :email";
        Long count = em.createQuery(jpql, Long.class)
                        .setParameter("email", customerEntity.getEmail())
                                .getSingleResult();

        if(count > 0)
            throw new IllegalArgumentException("Email já existe");

        em.persist(customerEntity);
        System.out.println(customerEntity);

    }
}
