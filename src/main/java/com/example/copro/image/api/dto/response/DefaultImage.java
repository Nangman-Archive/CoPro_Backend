package com.example.copro.image.api.dto.response;

import org.springframework.beans.factory.annotation.Value;

public enum DefaultImage {
   DEFAULT_IMAGE("https://d35sohbc9et6k7.cloudfront.net/coproimage/35324cfc-e04a-4559-beab-f41a9c23d759_KakaoTalk_20231030_161509818.jpg");

    DefaultImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String imageUrl;


}
