package com.example.copro.board.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(String message){
        super(message);
    }

    public CategoryNotFoundException(){
        this("잘못된 카테고리입니다.");
    }
}
