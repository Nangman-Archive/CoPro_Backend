package com.example.copro.board.domain.repository;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.image.domain.Image;
import com.example.copro.member.domain.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>,BoardCustomRepository {
    Page<Board> findAllByCategory(Category category, Pageable pageable);
    Page<Board> findByTitleContaining(String query, Pageable pageable);
    Board findByImagesContaining(Image image);
    @Query("SELECT b FROM Board b WHERE b.heart - b.previousHeartCount = (SELECT MAX(b2.heart - b2.previousHeartCount) FROM Board b2)")
    List<Board> findWithMaxIncreaseInHeart();
    Page<Board> findByMember(Member member, Pageable pageable);
}
