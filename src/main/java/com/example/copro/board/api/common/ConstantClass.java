package com.example.copro.board.api.common;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantClass {
    private static ConstantClass instance;

    @Value("${default-thumbnail-url}") //16번 이미지
    private String defaultImageUrl;

    @PostConstruct
    public void init() {
        instance = this;
    } //객체초기화

    public static ConstantClass getInstance() {
        return instance;
    } //생성한 객체 불러오기

    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }
}