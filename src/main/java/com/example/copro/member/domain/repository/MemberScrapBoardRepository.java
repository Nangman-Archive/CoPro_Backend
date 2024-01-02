package com.example.copro.member.domain.repository;

import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberScrapBoard;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberScrapBoardRepository extends JpaRepository<MemberScrapBoard, Long> {
    Optional<MemberScrapBoard> findByMemberMemberIdAndBoardBoardId(Long memberId, Long boardId);

    Page<MemberScrapBoard> findByMember(Member member, Pageable pageable);
}
