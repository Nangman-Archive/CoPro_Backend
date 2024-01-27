package com.example.copro.board.exception;

import com.example.copro.global.error.exception.NotFoundGroupException;

public class HeartNotFoundException extends NotFoundGroupException {
    public HeartNotFoundException(String message){
        super(message);
    }

    public HeartNotFoundException(){
        this("좋아요 정보가 존재하지 않습니다.");
    }
}
