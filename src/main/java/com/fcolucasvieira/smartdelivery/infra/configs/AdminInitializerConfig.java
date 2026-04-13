package com.fcolucasvieira.smartdelivery.infra.configs;

import com.fcolucasvieira.smartdelivery.modules.users.usecases.CreateUserUseCase;
import com.fcolucasvieira.smartdelivery.modules.users.entity.enums.UserRole;
import com.fcolucasvieira.smartdelivery.modules.users.dto.CreateUserRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// (Configuration) - Inicializar Admin da aplicação
@Configuration
public class AdminInitializerConfig {
    // (CommandLineRunner) - Útil para execução de código após a inicialização completa da aplicação
    // Ideal para tarefas de inicialização como carregamento de dados iniciais em banco de dados
    @Bean
    public CommandLineRunner initAdmin(CreateUserUseCase createUserUseCase){
        return args -> {
            CreateUserRequest request = CreateUserRequest.builder()
                    .username("admin@smartdelivery.com")
                    .password("admin123")
                    .userRole(UserRole.ADMIN)
                    .build();

            createUserUseCase.execute(request);
        };
    }
}
