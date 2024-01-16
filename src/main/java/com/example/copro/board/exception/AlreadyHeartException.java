package com.example.copro.board.exception;

public class AlreadyHeartException extends RuntimeException{
    public AlreadyHeartException(String message){
        super(message);
    }

    public AlreadyHeartException(){
        this("이미 좋아요를 눌렀습니다.");
    }

}
