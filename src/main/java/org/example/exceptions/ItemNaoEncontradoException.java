package org.example.exceptions;

public class ItemNaoEncontradoException extends RuntimeException {
    public ItemNaoEncontradoException(String message) {
        super(message);
    }
}