package com.hugo.blockbookingapi.exception;

public class BookingConflictException extends Exception {
    public BookingConflictException(String message) {
        super(message);
    }
}