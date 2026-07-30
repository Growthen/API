package dev.growthen.apilibreria.common.exception;

import dev.growthen.apilibreria.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Construye una respuesta estandarizada de error envuelta en un
     * {@link ResponseEntity}.
     *
     * @param message mensaje descriptivo para el cliente o usuario
     * @param error   código o categoría corta del error
     * @param status  código de estado HTTP a retornar
     * @param path    URI o ruta del endpoint donde ocurrió la excepción
     * @return un {@code ResponseEntity} conteniendo el payload
     *         {@link ErrorResponse} y el status correspondiente
     */
    private ResponseEntity<ErrorResponse> buildResponse(String message, String error, HttpStatus status, String path) {
        ErrorResponse response = new ErrorResponse(message, error, status.value(), path, LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), "FORBIDDEN", HttpStatus.FORBIDDEN, request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
            HttpServletRequest request) {
        return buildResponse(ex.getMessage(), "NOT_FOUND", HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex,
            HttpServletRequest request) {
        return buildResponse(ex.getMessage(), "CONFLICT", HttpStatus.CONFLICT, request.getRequestURI());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), "BAD_REQUEST", HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        
        return buildResponse(errors.toString(), "VALIDATION_FAILED", HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), "UNAUTHORIZED", HttpStatus.UNAUTHORIZED, request.getRequestURI());
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(org.springframework.security.core.AuthenticationException ex, HttpServletRequest request) {
        return buildResponse("Authentication failed: " + ex.getMessage(), "UNAUTHORIZED", HttpStatus.UNAUTHORIZED, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace(); // Logs the error in console for debugging
        return buildResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }
}
