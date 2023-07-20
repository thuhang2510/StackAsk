package com.hang.stackask.exception;

public class VerificationTokenNotFoundException extends RuntimeException {
    public VerificationTokenNotFoundException(String msg) {
        super(msg);
    }
}
