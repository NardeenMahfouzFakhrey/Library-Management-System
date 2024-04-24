package com.library.exception;

public class BookViolateConstraintException extends RuntimeException {
    public BookViolateConstraintException(String message) {
        super(message);
    }

    public BookViolateConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
