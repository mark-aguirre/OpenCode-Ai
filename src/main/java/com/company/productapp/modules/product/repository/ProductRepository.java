package com.company.productapp.modules.product.repository;

import com.company.productapp.modules.product.domain.Product;
import com.company.productapp.modules.product.domain.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Product entities.
 *
 * @author Product Management Team
 * @version 1.0
 * @since 2026-01-22
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds a product by its Stock Keeping Unit (SKU).
     *
     * @param sku the SKU to search for, case-sensitive exact match
     * @return an Optional containing the product if found, otherwise empty
     */
    Optional<Product> findBySku(String sku);

    /**
     * Finds a product by its exact name.
     *
     * @param name the product name to search for, case-sensitive exact match
     * @return an Optional containing the product if found, otherwise empty
     */
    Optional<Product> findByName(String name);

    /**
     * Finds all products belonging to a specific category.
     *
     * @param category the product category to filter by
     * @return a list of products in the specified category, empty if none found
     */
    List<Product> findByCategory(ProductCategory category);

    /**
     * Finds products belonging to a specific category with pagination support.
     *
     * @param category the product category to filter by
     * @param pageable pagination and sorting configuration
     * @return a Page containing products in the specified category
     */
    Page<Product> findByCategory(ProductCategory category, Pageable pageable);

    /**
     * Searches products across name, description, and SKU fields.
     *
     * @param searchTerm the text to search for across multiple fields
     * @param pageable pagination and sorting configuration
     * @return a Page containing products matching the search criteria
     */
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Searches products within a specific category across name and description fields.
     *
     * @param category the category to search within
     * @param searchTerm the text to search for within the category
     * @param pageable pagination and sorting configuration
     * @return a Page containing products matching both category and search criteria
     */
    @Query("SELECT p FROM Product p WHERE " +
           "p.category = :category AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Product> searchProductsByCategory(
            @Param("category") ProductCategory category,
            @Param("searchTerm") String searchTerm,
            Pageable pageable);

    /**
     * Finds products with stock quantity below the specified threshold.
     *
     * @param threshold the maximum stock quantity (exclusive)
     * @return a list of products with stock less than the threshold
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    /**
     * Finds all products with zero stock quantity.
     *
     * @return a list of out-of-stock products
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0")
    List<Product> findOutOfStockProducts();

    /**
     * Checks if a product with the given SKU exists.
     *
     * @param sku the SKU to check for existence
     * @return true if a product with the SKU exists, false otherwise
     */
    boolean existsBySku(String sku);

    /**
     * Checks if a product with the given name exists.
     *
     * @param name the product name to check for existence
     * @return true if a product with the name exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Counts the total number of products in a specific category.
     *
     * @param category the category to count products for
     * @return the total number of products in the category
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category")
    long countByCategory(@Param("category") ProductCategory category);

    /**
     * Retrieves product count statistics grouped by category.
     *
     * @return a list of category-count pairs, empty if no products exist
     */
    @Query("SELECT p.category, COUNT(p) FROM Product p GROUP BY p.category")
    List<Object[]> countProductsByCategory();
}