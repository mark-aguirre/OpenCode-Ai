package com.company.productapp.modules.product.service;

import com.company.productapp.modules.product.domain.Product;
import com.company.productapp.modules.product.domain.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing product operations.
 *
 * @author Product Management Team
 * @version 1.0
 * @since 2026-01-22
 */
public interface ProductService {

    /**
     * Creates a new product in the system.
     *
     * @param product the product entity to be created, must not be null
     * @return the created product with generated ID and timestamps
     * @throws DuplicateResourceException if a product with the same SKU or name already exists
     */
    Product createProduct(Product product);

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product, must not be null
     * @return an Optional containing the product if found, otherwise empty
     */
    Optional<Product> getProductById(Long id);

    /**
     * Retrieves a product by its Stock Keeping Unit (SKU).
     *
     * @param sku the SKU of the product to find, must not be null or blank
     * @return an Optional containing the product if found, otherwise empty
     */
    Optional<Product> getProductBySku(String sku);

    /**
     * Retrieves all products with pagination support.
     *
     * @param pageable pagination and sorting information, must not be null
     * @return a Page containing products for the requested page
     */
    Page<Product> getAllProducts(Pageable pageable);

    /**
     * Retrieves all products belonging to a specific category.
     *
     * @param category the product category to filter by, must not be null
     * @return a list of products in the specified category, empty if none found
     */
    List<Product> getProductsByCategory(ProductCategory category);

    /**
     * Retrieves products belonging to a specific category with pagination.
     *
     * @param category the product category to filter by, must not be null
     * @param pageable pagination and sorting information, must not be null
     * @return a Page containing products in the specified category
     */
    Page<Product> getProductsByCategory(ProductCategory category, Pageable pageable);

    /**
     * Updates an existing product with new information.
     *
     * @param id the unique identifier of the product to update, must not be null
     * @param product the updated product information, must not be null
     * @return the updated product with new timestamp
     * @throws ResourceNotFoundException if the product with given ID doesn't exist
     * @throws DuplicateResourceException if updates conflict with existing products
     */
    Product updateProduct(Long id, Product product);

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id the unique identifier of the product to delete, must not be null
     * @throws ResourceNotFoundException if the product with given ID doesn't exist
     */
    void deleteProduct(Long id);

    /**
     * Searches products based on a search term.
     *
     * @param searchTerm the text to search for, must not be null or blank
     * @param pageable pagination and sorting information, must not be null
     * @return a Page containing products matching the search criteria
     */
    Page<Product> searchProducts(String searchTerm, Pageable pageable);

    /**
     * Searches products within a specific category based on a search term.
     *
     * @param category the category to search within, must not be null
     * @param searchTerm the text to search for within the category, must not be null or blank
     * @param pageable pagination and sorting information, must not be null
     * @return a Page containing products matching both category and search criteria
     */
    Page<Product> searchProductsByCategory(ProductCategory category, String searchTerm, Pageable pageable);

    /**
     * Retrieves products with stock quantity below the specified threshold.
     *
     * @param threshold the maximum stock quantity to include (exclusive), must be positive
     * @return a list of products with stock less than the threshold, empty if none
     */
    List<Product> getLowStockProducts(Integer threshold);

    /**
     * Retrieves all products with zero stock quantity.
     *
     * @return a list of out-of-stock products, empty if none
     */
    List<Product> getOutOfStockProducts();

    /**
     * Checks if a product with the given SKU already exists.
     *
     * @param sku the SKU to check for existence, must not be null
     * @return true if a product with the SKU exists, false otherwise
     */
    boolean existsBySku(String sku);

    /**
     * Checks if a product with the given name already exists.
     *
     * @param name the product name to check for existence, must not be null
     * @return true if a product with the name exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Counts the total number of products in a specific category.
     *
     * @param category the category to count products for, must not be null
     * @return the total number of products in the category
     */
    long countProductsByCategory(ProductCategory category);

    /**
     * Retrieves product count statistics grouped by category.
     *
     * @return a list of category-count pairs, empty if no products exist
     */
    List<Object[]> getProductCountByCategory();
}