package com.hang.stackask.exception;

public class NoDeletePermissionException extends RuntimeException {
    public NoDeletePermissionException(String message) {
        super(message);
    }
}
