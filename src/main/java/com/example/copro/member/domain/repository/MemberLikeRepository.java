package com.example.copro.member.domain.repository;

import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLikeRepository extends JpaRepository<MemberLike, Long> {
    boolean existsByMemberAndLikedMember(Member member, Member memberLike);
    boolean existsByMemberAndLikedMember(Member member, MemberLike memberLike);

    @Query("select count(ml) "
            + "from MemberLike ml "
            + "where ml.likedMember = :currentMember ")
    int countByLikedMember(@Param("currentMember") Member currentMember);

    @Query("select count(ml) "
            + "from MemberLike ml "
            + "where ml.likedMember = :memberLike ")
    int countByLikedMember(@Param("memberLike") MemberLike currentMember);

    Page<MemberLike> findByMember(Member member, Pageable pageable);
}
