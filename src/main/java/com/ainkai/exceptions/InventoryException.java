package com.ainkai.exceptions;

import lombok.Getter;

@Getter
public class InventoryException extends Exception {

    private static final long serialVersionUID = 1L;
    private final String messageKey;

    public InventoryException(String messageKey, String message, Throwable cause) {
        super(message, cause);
        this.messageKey = messageKey;
    }

    public InventoryException(String messageKey, String message) {
        super(message);
        this.messageKey = messageKey;
    }
}
