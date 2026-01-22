package com.company.productapp.modules.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing a product in the inventory management system.
 *
 * @author Product Management Team
 * @version 1.0
 * @since 2026-01-22
 * @see ProductCategory
 */
@Entity
@Table(name = "products", 
       indexes = {
           @Index(name = "idx_products_name", columnList = "name"),
           @Index(name = "idx_products_sku", columnList = "sku"),
           @Index(name = "idx_products_category", columnList = "category")
       },
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"sku"}, name = "uk_products_sku"),
           @UniqueConstraint(columnNames = {"name"}, name = "uk_products_name")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /**
     * Unique identifier for the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The display name of the product.
     */
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    /**
     * Detailed description of the product.
     */
    @Size(max = 500, message = "Product description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

    /**
     * The selling price of the product.
     */
    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.01", message = "Product price must be at least 0.01")
    @DecimalMax(value = "99999.99", message = "Product price must not exceed 99999.99")
    @Digits(integer = 5, fraction = 2, message = "Product price must have at most 5 integer digits and 2 fraction digits")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Stock Keeping Unit - unique product identifier.
     */
    @NotBlank(message = "Product SKU is required")
    @Size(min = 3, max = 20, message = "Product SKU must be between 3 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Product SKU must contain only uppercase letters, numbers, and hyphens")
    @Column(name = "sku", nullable = false, unique = true, length = 20)
    private String sku;

    /**
     * The category classification for the product.
     *
     * @see ProductCategory
     */
    @NotNull(message = "Product category is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private ProductCategory category;

    /**
     * Current inventory quantity available for sale.
     */
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    /**
     * Timestamp when the product was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the product was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * JPA lifecycle callback executed before entity persistence.
     */
    @PrePersist
    protected void onCreate() {
        if (stockQuantity == null) {
            stockQuantity = 0;
        }
    }

    /**
     * Determines equality between Product entities.
     *
     * @param o the object to compare with
     * @return true if the objects represent the same product
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && 
               Objects.equals(sku, product.sku);
    }

    /**
     * Generates hash code for the Product entity.
     *
     * @return hash code value for this product
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, sku);
    }

    /**
     * String representation of the Product entity.
     *
     * @return string representation of the product
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}