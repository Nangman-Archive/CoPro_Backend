package com.example.copro.member.exception;

public class ExistsNickNameException extends RuntimeException{
    public ExistsNickNameException(String message) {
        super(message);
    }

    public ExistsNickNameException() {
        this("이미 존재하는 닉네임입니다.");
    }
}
