package com.hang.stackask.exception;

public class VerificationTokenTimeExpiredException extends RuntimeException {
    public VerificationTokenTimeExpiredException(String msg) {
        super(msg);
    }
}
