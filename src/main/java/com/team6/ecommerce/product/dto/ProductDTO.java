package com.team6.ecommerce.product.dto;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProductDTO {

    @Nonnull
    private String title;

    @Nonnull
    private String categoryId;

    @Nonnull
    private String brand;

    @Nonnull
    private String model;

    @Nonnull
    private String serialNumber;

    @Nonnull
    private String description;

    @Nonnull
    @Min(0)
    private int quantityInStock;

    @Nonnull
    @Min(0)
    private double basePrice;

    @Nonnull
    private boolean warrantyStatus;

    @Nonnull
    private String distributorId;

}
