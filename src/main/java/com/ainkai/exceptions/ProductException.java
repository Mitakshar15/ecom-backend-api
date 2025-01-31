package com.ainkai.exceptions;

import lombok.Getter;

@Getter
public class ProductException extends Exception{

    private static final long serialVersionUID = 1L;
    private final String messageKey;

    public ProductException(String messageKey, String message, Throwable cause) {
        super(message, cause);
        this.messageKey = messageKey;
    }

    public ProductException(String messageKey, String message) {
        super(message);
        this.messageKey = messageKey;
    }

}
