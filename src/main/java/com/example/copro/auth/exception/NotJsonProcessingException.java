package com.example.copro.auth.exception;

import com.example.copro.global.error.exception.AuthGroupException;

public class NotJsonProcessingException extends AuthGroupException {
    public NotJsonProcessingException(String message) {
        super(message);
    }

    public NotJsonProcessingException() {
        this("id 토큰을 읽을 수 없습니다.");
    }
}
