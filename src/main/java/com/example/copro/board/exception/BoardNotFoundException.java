package com.example.copro.board.exception;

import com.example.copro.global.error.exception.NotFoundGroupException;

public class BoardNotFoundException extends NotFoundGroupException {
    public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException(Long boardId) {
        this("해당하는 게시판이 없습니다. BoardID: " + boardId);
    }

}
