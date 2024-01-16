package com.example.copro.board.exception;

public class NotOwnerException extends RuntimeException{
    public NotOwnerException(String message){
        super(message);
    }

    public NotOwnerException() {
        this("게시물의 소유자만 수정, 삭제할 수 있습니다.");
    }

}
