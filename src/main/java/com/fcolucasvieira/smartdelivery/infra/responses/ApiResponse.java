package com.fcolucasvieira.smartdelivery.infra.responses;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        String status,
        String message,
        T data,
        LocalDateTime timestamp
) {

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("success", message, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(T data, String message) {
        return new ApiResponse<>("error", message, data, LocalDateTime.now());
    }
}
