package com.ainkai.exceptions;

import lombok.Getter;

@Getter
public class OrderException extends Exception {

    private static final long serialVersionUID = 1L;
    private final String messageKey;

    public OrderException(String messageKey, String message, Throwable cause) {
        super(message, cause);
        this.messageKey = messageKey;
    }

    public OrderException(String messageKey, String message) {
        super(message);
        this.messageKey = messageKey;
    }

}
