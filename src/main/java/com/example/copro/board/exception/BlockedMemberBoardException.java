package com.example.copro.board.exception;

import com.example.copro.global.error.exception.InvalidGroupException;

public class BlockedMemberBoardException extends InvalidGroupException {
    public BlockedMemberBoardException(String message){
        super(message);
    }

    public BlockedMemberBoardException(){
        this("차단한 사용자의 게시물입니다.");
    }
}
