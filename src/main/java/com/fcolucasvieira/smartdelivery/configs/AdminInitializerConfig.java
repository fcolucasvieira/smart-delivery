package com.fcolucasvieira.smartdelivery.configs;

import com.fcolucasvieira.smartdelivery.modules.users.CreateUserUseCase;
import com.fcolucasvieira.smartdelivery.modules.users.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

// (Configuration) - Inicializar Admin da aplicação
@Configuration
public class AdminInitializerConfig {
    // (CommandLineRunner) - Útil para execução de código após a inicialização completa da aplicação
    // Ideal para tarefas de inicialização como carregamento de dados iniciais em banco de dados
    // @Bean
    public CommandLineRunner initAdmin(CreateUserUseCase createUserUseCase){
        return args -> {
            createUserUseCase.execute(
                    "admin@samrtdelivery.com",
                    "admin123",
                    Role.ADMIN
            );
        };
    }
}
