package com.example.copro.board.exception;

import com.example.copro.global.error.exception.InvalidGroupException;

public class AlreadyHeartException extends InvalidGroupException {
    public AlreadyHeartException(String message){
        super(message);
    }

    public AlreadyHeartException(){
        this("이미 좋아요를 눌렀습니다.");
    }

}
