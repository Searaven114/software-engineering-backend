package com.team6.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private String status;
    private String message;
    private Object data;
}
