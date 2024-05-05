package com.example.copro.member.domain.repository;

import com.example.copro.member.domain.BlockedMemberMapping;
import com.example.copro.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedMemberMappingRepository extends JpaRepository<BlockedMemberMapping, Long> {
    Page<BlockedMemberMapping> findByMember(Member member, Pageable pageable);
    boolean existsByMemberAndBlockedMember(Member member, Member blockedMember);

}
