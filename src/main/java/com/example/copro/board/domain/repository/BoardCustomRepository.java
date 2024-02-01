package com.example.copro.board.domain.repository;

import com.example.copro.board.api.dto.response.BoardDto;
import com.example.copro.board.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    Page<BoardDto> findAllWithCommentCount(Category category, Pageable pageable);

    Page<BoardDto> findByTitleContaining(String query, Pageable pageable);
}
