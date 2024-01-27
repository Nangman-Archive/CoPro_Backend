package com.example.copro.member.exception;

import com.example.copro.global.error.exception.InvalidGroupException;

public class InvalidNickNameAddressException extends InvalidGroupException {
    public InvalidNickNameAddressException(String message) {
        super(message);
    }

    public InvalidNickNameAddressException() {
        this("닉네임 형식이 올바르지 않습니다.");
    }
}
