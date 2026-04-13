package com.fcolucasvieira.smartdelivery.infra.security.controller;

import com.fcolucasvieira.smartdelivery.infra.security.dto.LoginUserResponse;
import com.fcolucasvieira.smartdelivery.infra.security.dto.LoginUserRequest;
import com.fcolucasvieira.smartdelivery.modules.users.usecases.LoginUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginUserUseCase useCase;

    @Operation(
            summary = "Authenticate user and generate JWT",
            description = "Authenticates a user with username and password. Returns a JWT token that must be used to access protected endpoints. No authentication required to call this endpoint."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody @Valid LoginUserRequest request) {
        LoginUserResponse response = this.useCase.execute(request);

        return ResponseEntity.ok(response);
    }
}
