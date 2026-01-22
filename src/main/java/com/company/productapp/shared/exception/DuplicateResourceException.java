package com.company.productapp.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when attempting to create or update a resource that violates uniqueness constraints.
 * 
 * <p>This exception is typically thrown when trying to create a resource with an identifier
 * (such as SKU or name) that already exists in the system, or when updating a resource
 * to use an identifier that conflicts with another existing resource.</p>
 * 
 * <p>Maps to HTTP 409 Conflict status code, indicating that the request could not be
 * completed due to a conflict with the current state of the target resource.</p>
 * 
 * <p>Common usage scenarios:</p>
 * <ul>
 *   <li>Creating a product with an existing SKU</li>
 *   <li>Creating a product with an existing name</li>
 *   <li>Updating a product's SKU to one that's already in use</li>
 *   <li>Updating a product's name to one that's already in use</li>
 * </ul>
 * 
 * <p>This is an unchecked exception that should be caught by the global
 * exception handler and converted to an appropriate HTTP response.</p>
 * 
 * @author Product Management Team
 * @version 1.0
 * @since 2026-01-06
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {
    
    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     * 
     * @param message the detail message explaining which resource is duplicated
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new DuplicateResourceException with the specified detail message and cause.
     * 
     * @param message the detail message explaining which resource is duplicated
     * @param cause the cause of this exception
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}