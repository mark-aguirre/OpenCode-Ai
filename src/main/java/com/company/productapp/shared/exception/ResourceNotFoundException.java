package com.company.productapp.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource cannot be found.
 * 
 * <p>This exception is typically thrown when attempting to retrieve,
 * update, or delete a resource (such as a product) that doesn't exist
 * in the system. It maps to HTTP 404 Not Found status code.</p>
 * 
 * <p>Common usage scenarios:</p>
 * <ul>
 *   <li>Looking up a product by non-existent ID</li>
 *   <li>Attempting to update a product that was deleted</li>
 *   <li>Trying to delete a product that doesn't exist</li>
 * </ul>
 * 
 * <p>This is an unchecked exception that should be caught by the global
 * exception handler and converted to an appropriate HTTP response.</p>
 * 
 * @author Product Management Team
 * @version 1.0
 * @since 2026-01-22
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * 
     * @param message the detail message explaining why the exception occurred
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     * 
     * @param message the detail message explaining why the exception occurred
     * @param cause the cause of this exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}