package com.example.copro.board.exception;

import com.example.copro.global.error.exception.InvalidGroupException;

public class AlreadyScrapException extends InvalidGroupException {
    public AlreadyScrapException(String message){
        super(message);
    }

    public AlreadyScrapException(){
        this("이미 관심 목록에 추가되었습니다.");
    }
}
