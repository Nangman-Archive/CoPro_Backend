package com.example.copro.member.domain.repository;

import com.example.copro.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberName(String memberName);

    @Query(value = "select m "
            + "from Member m "
            + "where m.occupation = :occupation "
            + "or m.language = :language "
            + "or m.career = :career")
    List<Member> findTagAll(@Param("occupation") String occupation,
                            @Param("language") String language,
                            @Param("career") String career);
}
