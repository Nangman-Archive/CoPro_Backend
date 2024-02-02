package com.example.copro.board.exception;

import com.example.copro.global.error.exception.NotFoundGroupException;

public class ScrapNotFoundException extends NotFoundGroupException {
    public ScrapNotFoundException(String message){
        super(message);
    }

    public ScrapNotFoundException(){
        this("스크랩 정보가 존재하지 않습니다.");
    }
}
