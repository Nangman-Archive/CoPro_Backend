package com.example.copro.board.exception;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(Long memberId) {
        this(memberId + "번 멤버는 존재하지 않는 멤버입니다.");
    }
}
