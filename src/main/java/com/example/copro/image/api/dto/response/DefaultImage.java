package com.example.copro.image.api.dto.response;

public enum DefaultImage {
   DEFAULT_IMAGE("https://d35sohbc9et6k7.cloudfront.net/459a5af4-fc1b-4e02-a887-c8f61e84fc01_vackground-com-jEDNGHrrVRw-unsplash.jpg");

    public final String imageUrl;

    DefaultImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
