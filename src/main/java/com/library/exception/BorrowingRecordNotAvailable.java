package com.library.exception;

public class BorrowingRecordNotAvailable extends RuntimeException{
    public BorrowingRecordNotAvailable(String message) {
        super(message);
    }

}
