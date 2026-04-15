package com.fcolucasvieira.smartdelivery.infra.exceptions;

import com.fcolucasvieira.smartdelivery.infra.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Exceptions caused by Bean Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex){
        // Use struct Map<String, String> for organization information
        // Key = Field ; Value = Message
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage()
                ));

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(errors, "Validation failed"));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(null, ex.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleAlreadyExists(AlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(null, ex.getMessage()));
    }

    @ExceptionHandler(OrderEmptyException.class)
    public ResponseEntity<ApiResponse<String>> handleOrderEmpty(OrderEmptyException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(null, ex.getMessage()));
    }

    @ExceptionHandler(NoDeliveryManAvailableException.class)
    public ResponseEntity<ApiResponse<String>> handleNoDeliveryManAvailable(NoDeliveryManAvailableException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(null, ex.getMessage()));
    }

    @ExceptionHandler(InvalidStatusOrderException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidStatusOrder(InvalidStatusOrderException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(null, ex.getMessage()));
    }

    @ExceptionHandler(DeliveryManNotAssignedException.class)
    public ResponseEntity<ApiResponse<String>> handleDeliveryManNotAssigned(DeliveryManNotAssignedException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(null, ex.getMessage()));
    }
}
