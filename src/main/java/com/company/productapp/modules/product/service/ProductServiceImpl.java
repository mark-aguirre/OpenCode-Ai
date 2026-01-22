package com.company.productapp.modules.product.service;

import com.company.productapp.modules.product.domain.Product;
import com.company.productapp.modules.product.domain.ProductCategory;
import com.company.productapp.modules.product.repository.ProductRepository;
import com.company.productapp.shared.exception.DuplicateResourceException;
import com.company.productapp.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ProductService interface.
 *
 * @author Product Management Team
 * @version 1.0
 * @since 2026-01-22
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        log.info("Creating new product with name: {}, SKU: {}, category: {}", 
                product.getName(), product.getSku(), product.getCategory());
        
        log.debug("Validating product uniqueness before creation");
        if (existsBySku(product.getSku())) {
            log.warn("Product creation failed: SKU {} already exists", product.getSku());
            throw new DuplicateResourceException("Product with SKU " + product.getSku() + " already exists");
        }
        
        if (existsByName(product.getName())) {
            log.warn("Product creation failed: Name {} already exists", product.getName());
            throw new DuplicateResourceException("Product with name " + product.getName() + " already exists");
        }
        
        log.debug("Saving product to database");
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}, name: {}", savedProduct.getId(), savedProduct.getName());
        return savedProduct;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        log.debug("Finding product by ID: {}", id);
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductBySku(String sku) {
        log.debug("Finding product by SKU: {}", sku);
        return productRepository.findBySku(sku);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        log.debug("Fetching all products with pagination: {}", pageable);
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(ProductCategory category) {
        log.debug("Finding products by category: {}", category);
        return productRepository.findByCategory(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(ProductCategory category, Pageable pageable) {
        log.debug("Finding products by category {} with pagination: {}", category, pageable);
        return productRepository.findByCategory(category, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product updateProduct(Long id, Product product) {
        log.info("Updating product with ID: {}", id);
        log.debug("Update data - Name: {}, SKU: {}, Category: {}, Price: {}, Stock: {}", 
                product.getName(), product.getSku(), product.getCategory(), 
                product.getPrice(), product.getStockQuantity());
        
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product update failed: Product with ID {} not found", id);
                    return new ResourceNotFoundException("Product not found with ID: " + id);
                });

        log.debug("Checking for conflicts with existing products");
        if (!existingProduct.getSku().equals(product.getSku()) && existsBySku(product.getSku())) {
            log.warn("Product update failed: SKU {} already exists", product.getSku());
            throw new DuplicateResourceException("Product with SKU " + product.getSku() + " already exists");
        }
        
        if (!existingProduct.getName().equals(product.getName()) && existsByName(product.getName())) {
            log.warn("Product update failed: Name {} already exists", product.getName());
            throw new DuplicateResourceException("Product with name " + product.getName() + " already exists");
        }

        // Update all fields
        log.debug("Updating product fields");
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setSku(product.getSku());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setStockQuantity(product.getStockQuantity());

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated successfully with ID: {}, new name: {}", updatedProduct.getId(), updatedProduct.getName());
        return updatedProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        
        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String searchTerm, Pageable pageable) {
        log.debug("Searching products with term: {}", searchTerm);
        return productRepository.searchProducts(searchTerm, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchProductsByCategory(ProductCategory category, String searchTerm, Pageable pageable) {
        log.debug("Searching products in category {} with term: {}", category, searchTerm);
        return productRepository.searchProductsByCategory(category, searchTerm, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts(Integer threshold) {
        log.debug("Finding products with stock less than: {}", threshold);
        return productRepository.findLowStockProducts(threshold);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getOutOfStockProducts() {
        log.debug("Finding out of stock products");
        return productRepository.findOutOfStockProducts();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return productRepository.existsBySku(sku);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public long countProductsByCategory(ProductCategory category) {
        return productRepository.countByCategory(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getProductCountByCategory() {
        return productRepository.countProductsByCategory();
    }
}