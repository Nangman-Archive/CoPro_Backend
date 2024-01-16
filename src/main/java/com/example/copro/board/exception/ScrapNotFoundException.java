package com.example.copro.board.exception;

public class ScrapNotFoundException extends RuntimeException{
    public ScrapNotFoundException(String message){
        super(message);
    }

    public ScrapNotFoundException(){
        this("스크랩 정보가 존재하지 않습니다.");
    }
}
