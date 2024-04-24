package com.library.exception;

public class PatronViolateConstraintException extends RuntimeException {
    public PatronViolateConstraintException(String message) {
        super(message);
    }

    public PatronViolateConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
