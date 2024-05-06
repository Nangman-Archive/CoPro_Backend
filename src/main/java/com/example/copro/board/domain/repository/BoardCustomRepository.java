package com.example.copro.board.domain.repository;

import com.example.copro.board.api.dto.response.BoardDto;
import com.example.copro.board.domain.Category;
import com.example.copro.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    Page<BoardDto> findAllWithCommentCount(Category category, Pageable pageable, Member member);

    Page<BoardDto> findByTitleContaining(String query, Pageable pageable, Member member);
}
