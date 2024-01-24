package com.example.copro.board.exception;

public class NotBoardOwnerException extends RuntimeException{
    public NotBoardOwnerException(String message){
        super(message);
    }

    public NotBoardOwnerException() {
        this("게시물의 소유자만 수정, 삭제할 수 있습니다.");
    }

}
