package com.team6.ecommerce.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank
    @Size(min = 3, message = "Product name must contain at least 3 characters")
    private String title;

    @Indexed(unique = true)
    @NotBlank
    private String categoryId;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private String serialNumber;

    private String description;
    private int quantityInStock;

    @Min(value = 0, message = "Base price cannot be lower than 0")
    private double basePrice;

    private boolean warrantyStatus;
    private String distributorId;

    // Popularity metric, updated based on purchases
    private double popularityPoint;

    // New field for storing image as binary data
    private byte[] image;

    // Constructor without id (used when creating new products)
    public Product(String title, String categoryId, String brand, String model, String serialNumber, String description, int quantityInStock, double basePrice, boolean warrantyStatus, String distributorId, byte[] image) {
        this.title = title;
        this.categoryId = categoryId;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.description = description;
        this.quantityInStock = quantityInStock;
        this.basePrice = basePrice;
        this.warrantyStatus = warrantyStatus;
        this.distributorId = distributorId;
        this.popularityPoint = 0;
        this.image = image;
    }

    // Constructor with id (used when retrieving or updating existing products)
    public Product(String id, String title, String categoryId, String brand, String model, String serialNumber, String description, int quantityInStock, double basePrice, boolean warrantyStatus, String distributorId, byte[] image) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.description = description;
        this.quantityInStock = quantityInStock;
        this.basePrice = basePrice;
        this.warrantyStatus = warrantyStatus;
        this.distributorId = distributorId;
        this.popularityPoint = 0;
        this.image = image;
    }
}
