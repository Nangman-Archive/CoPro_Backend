package com.example.copro.image.exception;

public class NotFoundImageException extends RuntimeException {
    public NotFoundImageException(String fileName) {
        super(fileName + ": 이미지 업로드 실패");
    }
}
