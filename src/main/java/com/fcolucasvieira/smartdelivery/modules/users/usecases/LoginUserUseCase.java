package com.fcolucasvieira.smartdelivery.modules.users.usecases;

import com.fcolucasvieira.smartdelivery.infra.security.dto.LoginUserRequest;
import com.fcolucasvieira.smartdelivery.infra.security.dto.LoginUserResponse;
import com.fcolucasvieira.smartdelivery.infra.security.services.TokenService;
import com.fcolucasvieira.smartdelivery.modules.users.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCase {
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public LoginUserResponse execute(LoginUserRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        );

        var auth = authManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserEntity) auth.getPrincipal());

        return new LoginUserResponse(token);
    }
}
