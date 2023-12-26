package com.example.copro.member.exception;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException(final String message) {
        super(message);
    }

    public NotFoundMemberException() {
        this("멤버를 찾을 수 없습니다.");
    }
}