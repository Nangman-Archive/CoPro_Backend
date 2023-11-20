package com.example.copro.board.domain.repository;

import com.example.copro.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContaining(String q, Pageable pageable);

}
