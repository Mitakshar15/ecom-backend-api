package com.ainkai.exceptions;

import lombok.Getter;

@Getter
public class CartItemException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String messageKey;

    public CartItemException(String messageKey, String message, Throwable cause) {
        super(message, cause);
        this.messageKey = messageKey;
    }

    public CartItemException(String messageKey, String message) {
        super(message);
        this.messageKey = messageKey;
    }
}
