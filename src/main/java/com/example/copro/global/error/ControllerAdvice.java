package com.example.copro.global.error;

import com.example.copro.board.exception.AlreadyHeartException;
import com.example.copro.board.exception.AlreadyScrapException;
import com.example.copro.board.exception.BoardNotFoundException;
import com.example.copro.board.exception.CategoryNotFoundException;
import com.example.copro.board.exception.HeartNotFoundException;
import com.example.copro.board.exception.MappedImageException;
import com.example.copro.board.exception.NotBoardOwnerException;
import com.example.copro.board.exception.NotCommentOwnerException;
import com.example.copro.board.exception.ScrapNotFoundException;
import com.example.copro.comment.exception.CommentNotFoundException;
import com.example.copro.global.error.dto.ErrorResponse;
import com.example.copro.image.exception.ImageNotFoundException;
import com.example.copro.image.exception.UploadFailureImageException;
import com.example.copro.member.exception.ExistsLikeMemberException;
import com.example.copro.member.exception.ExistsNickNameException;
import com.example.copro.member.exception.InvalidGitHubUrlException;
import com.example.copro.member.exception.InvalidMemberException;
import com.example.copro.member.exception.MemberNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({
            ExistsLikeMemberException.class,
            ExistsNickNameException.class,
            InvalidMemberException.class,
            InvalidGitHubUrlException.class,
            UploadFailureImageException.class,
            AlreadyHeartException.class,
            AlreadyScrapException.class,
            MappedImageException.class,
            NotBoardOwnerException.class,
            NotCommentOwnerException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidData(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        log.error(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            MemberNotFoundException.class,
            BoardNotFoundException.class,
            ImageNotFoundException.class,
            CommentNotFoundException.class,
            CategoryNotFoundException.class,
            HeartNotFoundException.class,
            ScrapNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundDate(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        log.error(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
