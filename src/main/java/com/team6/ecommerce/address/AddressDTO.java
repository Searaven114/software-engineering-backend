package com.team6.ecommerce.address;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AddressDTO {

    private String street;

    private String city;

    private String zipCode;

    private String country;

    private String notes;

}
