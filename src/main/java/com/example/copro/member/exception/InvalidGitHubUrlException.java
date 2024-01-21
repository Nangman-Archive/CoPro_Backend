package com.example.copro.member.exception;

public class InvalidGitHubUrlException extends RuntimeException{
    public InvalidGitHubUrlException(final String message) {
        super(message);
    }

    public InvalidGitHubUrlException() {
        this("잘못된 깃 허브 주소입니다.");
    }
}
