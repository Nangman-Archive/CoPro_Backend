package com.example.copro.board.exception;

public class ImageCountExceededException extends RuntimeException{
    public ImageCountExceededException(String message){
        super(message);
    }

    public ImageCountExceededException(){
        this("총 이미지 개수는 최대 5개까지만 등록이 가능합니다.");
    }
}