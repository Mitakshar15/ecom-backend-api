package com.ainkai.exceptions;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final String messageKey;
    public UserException(String messageKey, String message) {
        super(message);
        this.messageKey = messageKey;
    }
}

