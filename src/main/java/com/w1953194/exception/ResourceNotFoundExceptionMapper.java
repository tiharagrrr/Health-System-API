/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.exception;

/**
 *
 * @author Tihara
 */



import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This class maps ResourceNotFoundException to a specific JAX-RS response.
 *
 * @Provider annotation indicates that this class can be automatically
 * discovered by the JAX-RS run time.
 */
@Provider
public class ResourceNotFoundExceptionMapper implements ExceptionMapper<ResourceNotFoundException> {

    /**
     * A logger instance for logging exceptions.
     */
    private static final Logger LOGGER = Logger.getLogger(ResourceNotFoundExceptionMapper.class.getName());

    /**
     * This method is invoked when a ResourceNotFoundException is thrown by a
     * JAX-RS resource.
     *
     * @param exception The ResourceNotFoundException that needs to be mapped to
     * a response.
     * @return A JAX-RS Response object representing the HTTP response to be
     * sent back to the client.
     */
    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        // Create a detailed error message with exception details
        String errorMessage = String.format("ResourceNotFoundException caught: %s, Exception: %s",
                exception.getMessage(), exception.getClass().getName());
        LOGGER.log(Level.SEVERE, errorMessage);  // Log the error message with severity level SEVERE

        // Build the JAX-RS response
        return Response.status(Response.Status.NOT_FOUND) // Set the status code to NOT_FOUND (404)
                .entity(exception.getMessage()) // Set the response entity (body) to the exception message
                .type(MediaType.TEXT_PLAIN) // Set the content type to plain text
                .build();                                         // Build the response object
    }
}
