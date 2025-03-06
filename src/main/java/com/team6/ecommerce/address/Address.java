package com.team6.ecommerce.address;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "addresses")
public class Address {

    @Id
    private String id;
    private String name;
    private String street;
    private String city;
    private String zipCode;
    private String country;
    private String notes;

    public Address(String street, String city, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Address(String name, String street, String city, String zipCode, String country, String notes) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
        this.notes = notes;
    }
}
