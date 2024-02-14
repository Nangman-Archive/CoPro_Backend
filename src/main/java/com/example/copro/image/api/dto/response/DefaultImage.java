package com.example.copro.image.api.dto.response;

import org.springframework.beans.factory.annotation.Value;

public enum DefaultImage {
   DEFAULT_IMAGE("https://d35sohbc9et6k7.cloudfront.net/4b95af21-e099-4964-9c46-647663b26201_KakaoTalk_20230526_234242506.jpg");

    final String imageUrl;

    DefaultImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
