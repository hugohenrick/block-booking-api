package com.hugo.blockbookingapi.exception;

public class BlockNotFoundException extends RuntimeException {

    public BlockNotFoundException(String message) {
        super(message);
    }
}
