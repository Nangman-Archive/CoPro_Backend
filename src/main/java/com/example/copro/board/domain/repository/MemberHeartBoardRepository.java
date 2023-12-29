package com.example.copro.board.domain.repository;

import com.example.copro.board.domain.MemberHeartBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberHeartBoardRepository extends JpaRepository<MemberHeartBoard, Long> {
    Optional<MemberHeartBoard> findByMemberMemberIdAndBoardBoardId(Long memberId, Long boardId);
}
