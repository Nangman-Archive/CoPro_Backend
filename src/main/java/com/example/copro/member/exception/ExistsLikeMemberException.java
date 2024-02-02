package com.example.copro.member.exception;

import com.example.copro.global.error.exception.InvalidGroupException;

public class ExistsLikeMemberException extends InvalidGroupException {
    public ExistsLikeMemberException(String message) {
        super(message);
    }

    public ExistsLikeMemberException() {
        this("이미 관심 목록에 추가 되었습니다.");
    }
}
