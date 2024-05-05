package com.example.copro.member.exception;

import com.example.copro.global.error.exception.InvalidGroupException;

public class ExistsBlockedMemberException extends InvalidGroupException {
    public ExistsBlockedMemberException(String message) {
        super(message);
    }

    public ExistsBlockedMemberException() {
        this("이미 차단 목록에 추가 되었습니다.");
    }
}
