package com.example.copro.board.exception;

public class AlreadyScrapException extends RuntimeException{
    public AlreadyScrapException(String message){
        super(message);
    }

    public AlreadyScrapException(){
        this("이미 관심 목록에 추가되었습니다.");
    }
}
