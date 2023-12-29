package com.example.copro.board.domain.repository;

import com.example.copro.board.domain.Board;
import com.example.copro.image.domain.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b from Board b left join fetch b.images")
    Page<Board> findAllWithImages(Pageable pageable);

    Page<Board> findByTitleContaining(String q, Pageable pageable);
    Board findByImagesContaining(Image image);

    @Query("SELECT b FROM Board b WHERE b.heart - b.previousHeartCount = (SELECT MAX(b2.heart - b2.previousHeartCount) FROM Board b2)")
    List<Board> findWithMaxIncreaseInHeart();
}
