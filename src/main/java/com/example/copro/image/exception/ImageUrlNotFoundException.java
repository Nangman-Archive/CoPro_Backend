package com.example.copro.image.exception;

import com.example.copro.global.error.exception.NotFoundGroupException;

public class ImageUrlNotFoundException extends NotFoundGroupException {
    public ImageUrlNotFoundException(String imageName){
        super("해당하는 이미지가 없습니다. URL: " + imageName);
    }
}
