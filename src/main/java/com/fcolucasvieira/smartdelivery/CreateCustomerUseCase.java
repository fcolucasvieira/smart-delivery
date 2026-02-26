package com.fcolucasvieira.smartdelivery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CreateCustomerUseCase {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void execute(CustomerEntity customerEntity){
        em.persist(customerEntity);
        System.out.println(customerEntity);

    }
}
