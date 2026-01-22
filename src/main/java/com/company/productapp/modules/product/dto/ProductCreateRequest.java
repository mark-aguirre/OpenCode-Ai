package com.company.productapp.modules.product.dto;

import com.company.productapp.modules.product.domain.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "Product creation request")
public record ProductCreateRequest(
    
    @Schema(description = "Product name", example = "Wireless Headphones")
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    String name,
    
    @Schema(description = "Product description", example = "High-quality wireless headphones with noise cancellation")
    @Size(max = 500, message = "Product description must not exceed 500 characters")
    String description,
    
    @Schema(description = "Product price", example = "99.99")
    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.01", message = "Product price must be at least 0.01")
    @DecimalMax(value = "99999.99", message = "Product price must not exceed 99999.99")
    @Digits(integer = 5, fraction = 2, message = "Product price must have at most 5 integer digits and 2 fraction digits")
    BigDecimal price,
    
    @Schema(description = "Product SKU", example = "WH-001-BLK")
    @NotBlank(message = "Product SKU is required")
    @Size(min = 3, max = 20, message = "Product SKU must be between 3 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Product SKU must contain only uppercase letters, numbers, and hyphens")
    String sku,
    
    @Schema(description = "Product category", example = "ELECTRONICS")
    @NotNull(message = "Product category is required")
    ProductCategory category,
    
    @Schema(description = "Stock quantity", example = "50")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    Integer stockQuantity
) {}