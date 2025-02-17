package br.com.onetalk.infrastructure.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class CustomTokenGenerationException extends WebApplicationException {
    public CustomTokenGenerationException(String message) {
        super(message);
    }

    public CustomTokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomTokenGenerationException(String message, Throwable cause, Response.Status response) {
        super(message, cause, response);
    }
}