package com.example.copro.member.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends RuntimeException{
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String MESSAGE = "존재하지 않는 사용자 입니다.";

    public HttpStatus getHttpStatus() {
        return HTTP_STATUS;
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }
}
