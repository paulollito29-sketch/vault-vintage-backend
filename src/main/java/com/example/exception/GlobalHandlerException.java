package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(StockInvalidException.class)
    public ResponseEntity<ApiError> handleStockInvalid(StockInvalidException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ApiError> handleInvalidDate(InvalidDateException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable() {
        return ResponseEntity.badRequest()
                .body(new ApiError("Incorrect Json Format"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("Verify the type path variable data type"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ErrorResponse(err.getField(), err.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    private record ApiError(String message) {}
}
