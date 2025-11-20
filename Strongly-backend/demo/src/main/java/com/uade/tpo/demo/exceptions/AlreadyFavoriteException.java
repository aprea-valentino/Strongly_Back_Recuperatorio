package com.uade.tpo.demo.exceptions;

public class AlreadyFavoriteException extends RuntimeException {
    public AlreadyFavoriteException(String message) {
        super(message);
    }
}