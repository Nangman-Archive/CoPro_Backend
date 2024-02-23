package com.example.copro.image.api.dto.response;

public enum DefaultImage {
   DEFAULT_IMAGE("https://d35sohbc9et6k7.cloudfront.net/007eb12d-f02a-42e0-981d-7950e185a209_DefaultImage.png");

    public final String imageUrl;

    DefaultImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
