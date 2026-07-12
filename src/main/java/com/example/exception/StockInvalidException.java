package com.example.exception;

public class StockInvalidException extends RuntimeException {
    public StockInvalidException(String message) {
        super(message);
    }
}
