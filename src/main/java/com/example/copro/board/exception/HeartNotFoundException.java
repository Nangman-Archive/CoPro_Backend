package com.example.copro.board.exception;

public class HeartNotFoundException extends RuntimeException{
    public HeartNotFoundException(String message){
        super(message);
    }

    public HeartNotFoundException(){
        this("좋아요 정보가 존재하지 않습니다.");
    }
}
