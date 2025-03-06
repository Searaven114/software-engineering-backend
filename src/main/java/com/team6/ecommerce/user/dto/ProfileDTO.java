package com.team6.ecommerce.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProfileDTO {

    private String email;
    private String name;
    private String surname;
    private String phone;
    private String age;
    private String registerDate; //Member Since: yyyy-mm-dd
}
