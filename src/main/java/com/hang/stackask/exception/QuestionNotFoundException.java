package com.hang.stackask.exception;

public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException(String msg) {
        super(msg);
    }
}
