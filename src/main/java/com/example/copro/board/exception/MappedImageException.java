package com.example.copro.board.exception;

import com.example.copro.global.error.exception.InvalidGroupException;
import com.example.copro.image.domain.Image;

public class MappedImageException extends InvalidGroupException {
    public MappedImageException(String message) {
        super(message);
    }

    public MappedImageException(Image image) {
        this(image.getId() + "번 이미지는 이미 매핑된 이미지입니다.");
    }
}
