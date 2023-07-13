package com.hang.stackask.exception;

public class PasswordMismatchException extends IllegalArgumentException {
    public PasswordMismatchException(String msg) {
        super(msg);
    }
}
