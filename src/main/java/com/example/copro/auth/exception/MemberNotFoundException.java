package com.example.copro.auth.exception;

public class MemberNotFoundException extends IllegalArgumentException {
    public MemberNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
