package com.ainkai.exceptions;

public class ProductException extends RuntimeException{

    private final String messageKey;
    public ProductException(String messageKey, String message) {
        super(message);
        this.messageKey = messageKey;
    }

}
