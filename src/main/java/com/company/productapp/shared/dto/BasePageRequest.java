package com.company.productapp.shared.dto;

import lombok.Data;

import jakarta.validation.constraints.Min;
import java.io.Serializable;

@Data
public abstract class BasePageRequest implements Serializable {
    @Min(value = 0, message = "Page number must be non-negative")
    private int page = 0;
    
    @Min(value = 1, message = "Page size must be at least 1")
    private int size = 10;
    
    private String sortBy = "id";
    private String sortDirection = "asc";
}