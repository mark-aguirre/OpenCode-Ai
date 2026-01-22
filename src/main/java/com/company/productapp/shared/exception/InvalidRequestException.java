package com.company.productapp.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a request contains invalid data or violates business rules.
 * 
 * <p>This exception is used for validation failures, business rule violations,
 * or other client-side errors that prevent the request from being processed.
 * It maps to HTTP 400 Bad Request status code.</p>
 * 
 * <p>Common usage scenarios:</p>
 * <ul>
 *   <li>Invalid data formats (e.g., malformed email, invalid dates)</li>
 *   <li>Business rule violations (e.g., negative prices, invalid categories)</li>
 *   <li>Required field omissions</li>
 *   <li>Data constraint violations not caught by validation annotations</li>
 * </ul>
 * 
 * <p>Note: This exception should typically be used for validations that
 * cannot be expressed through standard validation annotations or for
 * complex business logic validations.</p>
 * 
 * @author Product Management Team
 * @version 1.0
 * @since 2026-01-22
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
    
    /**
     * Constructs a new InvalidRequestException with the specified detail message.
     * 
     * @param message the detail message explaining why the request is invalid
     */
    public InvalidRequestException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new InvalidRequestException with the specified detail message and cause.
     * 
     * @param message the detail message explaining why the request is invalid
     * @param cause the cause of this exception
     */
    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}