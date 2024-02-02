package com.example.copro.board.exception;

import com.example.copro.global.error.exception.NotFoundGroupException;

public class CategoryNotFoundException extends NotFoundGroupException {
    public CategoryNotFoundException(String message){
        super(message);
    }

    public CategoryNotFoundException(){
        this("잘못된 카테고리입니다.");
    }
}
