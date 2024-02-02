package com.example.copro.image.exception;

import com.example.copro.global.error.exception.InvalidGroupException;

public class UploadFailureImageException extends InvalidGroupException {
    public UploadFailureImageException (String fileName) {
        super(fileName + ": 이미지 업로드 실패");
    }

}
