package com.example.copro.member.domain.repository;

import com.example.copro.member.domain.BlockedMemberMapping;
import com.example.copro.member.domain.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlockedMemberMappingRepository extends JpaRepository<BlockedMemberMapping, Long> {
    Page<BlockedMemberMapping> findByMember(Member member, Pageable pageable);
    boolean existsByMemberAndBlockedMember(Member member, Member blockedMember);

    @Query("select bm.blockedMember.memberId "
            + "from BlockedMemberMapping bm "
            + "where bm.member = :member ")
    List<Long> findByBlockedMemberId(@Param("member") Member member);
}
