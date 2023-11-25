package com.example.copro.member.exception;

public class ExistsLikeMemberException extends RuntimeException {
    public ExistsLikeMemberException(String message) {
        super(message);
    }

    public ExistsLikeMemberException() {
        this("이미 관심 목록에 추가 되었습니다.");
    }
}
