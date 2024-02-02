package com.example.copro.member.exception;

import com.example.copro.global.error.exception.InvalidGroupException;

public class InvalidGitHubUrlException extends InvalidGroupException {
    public InvalidGitHubUrlException(final String message) {
        super(message);
    }

    public InvalidGitHubUrlException() {
        this("잘못된 깃 허브 주소입니다.");
    }
}
