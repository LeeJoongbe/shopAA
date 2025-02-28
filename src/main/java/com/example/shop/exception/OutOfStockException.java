package com.example.shop.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String s) {
        super(s);
    }
}
