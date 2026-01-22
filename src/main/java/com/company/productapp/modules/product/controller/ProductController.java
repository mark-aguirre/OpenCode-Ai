package com.company.productapp.modules.product.controller;

import com.company.productapp.modules.product.domain.Product;
import com.company.productapp.modules.product.domain.ProductCategory;
import com.company.productapp.modules.product.service.ProductService;
import com.company.productapp.shared.exception.DuplicateResourceException;
import com.company.productapp.shared.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing product operations.
 *
 * @author Product Management Team
 * @version 1.0
 * @since 2026-01-22
 */
@RestController
@RequestMapping("/products")
@Tag(name = "Product Management", description = "APIs for managing products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * Constructs a new ProductController with the required service dependency.
     *
     * @param productService the service layer for product operations
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product in the system.
     *
     * @param product the product data to create, must be valid and unique
     * @return ResponseEntity with status 201 and created product data
     * @throws DuplicateResourceException if SKU or name already exists
     * @throws InvalidRequestException if validation fails
     */
    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully",
                content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Product with same SKU or name already exists")
    })
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        log.info("POST /api/products - Creating new product: name={}, sku={}, category={}", 
                product.getName(), product.getSku(), product.getCategory());
        
        try {
            Product createdProduct = productService.createProduct(product);
            log.info("POST /api/products - Product created successfully with ID: {}", createdProduct.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (DuplicateResourceException e) {
            log.warn("POST /api/products - Product creation failed due to conflict: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("POST /api/products - Unexpected error during product creation", e);
            throw e;
        }
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product to retrieve
     * @return ResponseEntity with status 200 and product data, or 404 if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a product by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found",
                content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        log.info("GET /api/products/{} - Retrieving product by ID", id);
        
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            log.info("GET /api/products/{} - Product found: {}", id, product.get().getName());
            return ResponseEntity.ok(product.get());
        } else {
            log.warn("GET /api/products/{} - Product not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Get product by SKU", description = "Retrieves a product by its SKU")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found",
                content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> getProductBySku(
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        Optional<Product> product = productService.getProductBySku(sku);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all products with pagination and sorting support.
     *
     * @param page zero-based page number (default: 0)
     * @param size number of items per page (default: 10)
     * @param sort field to sort by (default: "id")
     * @param direction sort direction: "asc" or "desc" (default: "asc")
     * @return ResponseEntity with status 200 and paginated product list
     */
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves a paginated list of all products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<Product>> getAllProducts(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        
        log.info("GET /api/products - Retrieving all products: page={}, size={}, sort={}, direction={}", 
                page, size, sort, direction);
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Product> products = productService.getAllProducts(pageable);
        
        log.info("GET /api/products - Retrieved {} products (page {} of {}, total elements: {})", 
                products.getNumberOfElements(), products.getNumber() + 1, products.getTotalPages(), products.getTotalElements());
        
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieves products belonging to a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @Parameter(description = "Product category") @PathVariable ProductCategory category,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Product> products = productService.getProductsByCategory(category, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Searches products by name or description")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
                content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<Product>> searchProducts(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Product> products = productService.searchProducts(searchTerm, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}/search")
    @Operation(summary = "Search products by category", description = "Searches products within a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
                content = @Content(schema = @Schema(implementation = Page.class)))
    })
    public ResponseEntity<Page<Product>> searchProductsByCategory(
            @Parameter(description = "Product category") @PathVariable ProductCategory category,
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Product> products = productService.searchProductsByCategory(category, searchTerm, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Retrieves products with stock quantity below threshold")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                content = @Content(schema = @Schema(implementation = Product.class)))
    })
    public ResponseEntity<List<Product>> getLowStockProducts(
            @Parameter(description = "Stock threshold") @RequestParam(defaultValue = "10") Integer threshold) {
        List<Product> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/out-of-stock")
    @Operation(summary = "Get out of stock products", description = "Retrieves products with zero stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                content = @Content(schema = @Schema(implementation = Product.class)))
    })
    public ResponseEntity<List<Product>> getOutOfStockProducts() {
        List<Product> products = productService.getOutOfStockProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing product with new details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
                content = @Content(schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "409", description = "Product with same SKU or name already exists")
    })
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Valid @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Deletes a product from the system by its unique identifier.
     *
     * @param id the unique identifier of the product to delete
     * @return ResponseEntity with status 204 if successful, or 404 if not found
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Deletes a product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        log.info("DELETE /api/products/{} - Deleting product", id);
        
        try {
            productService.deleteProduct(id);
            log.info("DELETE /api/products/{} - Product deleted successfully", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            log.warn("DELETE /api/products/{} - Product not found: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("DELETE /api/products/{} - Unexpected error during product deletion", id, e);
            throw e;
        }
    }

    @GetMapping("/statistics/count-by-category")
    @Operation(summary = "Get product count by category", description = "Retrieves product count grouped by category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<List<Object[]>> getProductCountByCategory() {
        List<Object[]> statistics = productService.getProductCountByCategory();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/exists/sku/{sku}")
    @Operation(summary = "Check if SKU exists", description = "Checks if a product with the given SKU exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ResponseEntity<Boolean> existsBySku(
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        boolean exists = productService.existsBySku(sku);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/name/{name}")
    @Operation(summary = "Check if name exists", description = "Checks if a product with the given name exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ResponseEntity<Boolean> existsByName(
            @Parameter(description = "Product name") @PathVariable String name) {
        boolean exists = productService.existsByName(name);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/category/{category}/count")
    @Operation(summary = "Count products by category", description = "Returns the total number of products in a category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> countProductsByCategory(
            @Parameter(description = "Product category") @PathVariable ProductCategory category) {
        long count = productService.countProductsByCategory(category);
        return ResponseEntity.ok(count);
    }
}