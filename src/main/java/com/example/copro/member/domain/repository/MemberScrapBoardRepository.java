package com.example.copro.member.domain.repository;

import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberScrapBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberScrapBoardRepository extends JpaRepository<MemberScrapBoard, Long> {
    Optional<MemberScrapBoard> findByMemberMemberIdAndBoardBoardId(Long memberId, Long boardId);
}
