package com.example.copro.board.domain.repository;

import com.example.copro.board.api.dto.response.BoardDto;
import com.example.copro.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    Page<BoardDto> findAllWithCommentCount(String category, Pageable pageable);
}
