package com.fcolucasvieira.smartdelivery.modules.users.usecases;

import com.fcolucasvieira.smartdelivery.core.exceptions.AlreadyExistsException;
import com.fcolucasvieira.smartdelivery.modules.users.entity.UserEntity;
import com.fcolucasvieira.smartdelivery.modules.users.mapper.UserMapper;
import com.fcolucasvieira.smartdelivery.modules.users.repository.UserRepository;
import com.fcolucasvieira.smartdelivery.modules.users.dto.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserEntity execute(CreateUserRequest request){
        validateUser(request);

        UserEntity user = UserMapper.toEntity(request, encoder);

        return this.repository.save(user);
    }

    private void validateUser(CreateUserRequest request) {
        this.repository.findByUsername(request.username())
                .ifPresent(user -> {
                    throw new AlreadyExistsException("User already exists with username: " + request.username());
                });
    }
}
