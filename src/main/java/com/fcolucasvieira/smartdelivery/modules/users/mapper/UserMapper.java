package com.fcolucasvieira.smartdelivery.modules.users.mapper;

import com.fcolucasvieira.smartdelivery.modules.users.entity.UserEntity;
import com.fcolucasvieira.smartdelivery.modules.users.dto.CreateUserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {
    public static UserEntity toEntity(CreateUserRequest request, PasswordEncoder encoder){
        return UserEntity.builder()
                .username(request.username())
                .password(encoder.encode(request.password()))
                .userRole(request.userRole())
                .build();
    }

}
