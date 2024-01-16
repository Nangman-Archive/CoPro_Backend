package com.example.copro.image.exception;

public class UploadFailureImageException extends RuntimeException {
    public UploadFailureImageException (String fileName) {
        super(fileName + ": 이미지 업로드 실패");
    }

}
