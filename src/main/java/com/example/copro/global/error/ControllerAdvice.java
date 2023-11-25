package com.example.copro.global.error;

import com.example.copro.global.error.dto.ErrorResponse;
import com.example.copro.member.exception.ExistsLikeMemberException;
import com.example.copro.member.exception.NotFoundMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({
            ExistsLikeMemberException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidData(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            NotFoundMemberException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundDate(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
