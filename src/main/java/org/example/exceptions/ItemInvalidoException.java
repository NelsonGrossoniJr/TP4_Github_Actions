package org.example.exceptions;

public class ItemInvalidoException extends RuntimeException {
    public ItemInvalidoException(String message) {
        super(message);
    }
}