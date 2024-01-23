package com.example.copro.board.exception;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException(Long boardId) {
        this("해당하는 게시판이 없습니다. BoardID: " + boardId);
    }

}
