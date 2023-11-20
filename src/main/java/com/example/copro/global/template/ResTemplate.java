package com.example.copro.global.template;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResTemplate<T> {
    int statusCode;
    String message;
    T data;

    public ResTemplate(HttpStatus httpStatus, String message, T data) {
        this.statusCode = httpStatus.value();
        this.message = message;
        this.data = data;
    }

    public ResTemplate(HttpStatus httpStatus, String message) {
        this.statusCode = httpStatus.value();
        this.message = message;
    }
}
