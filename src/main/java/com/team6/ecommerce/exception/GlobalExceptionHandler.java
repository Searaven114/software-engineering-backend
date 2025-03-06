package com.team6.ecommerce.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Unexpected Error(GlobalExceptionHandler)", ex.getMessage()));
    }


    @Data
    public static class ErrorResponse {

        private String error;
        private String message;
        private String timestamp = String.valueOf(System.currentTimeMillis());

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
            this.timestamp = String.valueOf(System.currentTimeMillis());
        }

    }
}

