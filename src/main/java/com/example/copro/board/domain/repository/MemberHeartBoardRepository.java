package com.example.copro.board.domain.repository;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.MemberHeartBoard;
import com.example.copro.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberHeartBoardRepository extends JpaRepository<MemberHeartBoard, Long> {
    Optional<MemberHeartBoard> findByMemberMemberIdAndBoardBoardId(Long memberId, Long boardId);

    boolean existsByMemberAndBoard(Member member, Board board);
}
