package com.example.copro.member.application;

import com.example.copro.member.domain.Member;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class MemberSpecs {
    public static Specification<Member> hasOccupation(String occupation) {
        return (member, cq, cb) -> occupation == null ? null : cb.equal(member.get("occupation"), occupation);
    }

    public static Specification<Member> hasLanguage(String language) {
        return (member, cq, cb) -> {
            if (language == null) {
                return null;
            }

            Predicate start = cb.like(member.get("language"), language + ",%");
            Predicate middle = cb.like(member.get("language"), "%," + language + ",%");
            Predicate end = cb.like(member.get("language"), "%," + language);
            Predicate only = cb.equal(member.get("language"), language);

            return cb.or(start, middle, end, only);
        };
    }

    public static Specification<Member> hasCareer(int career) {
        return (member, cq, cb) -> career < 1 ? null : cb.equal(member.get("career"), career);
    }

    public static Specification<Member> notCurrentMember(Member member) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("id"), member.getMemberId());
    }

    public static Specification<Member> isNotDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDelete"));
    }

    public static Specification<Member> isNicknameNotNull() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("nickName"));
    }

    public static Specification<Member> excludeAdminEmail(String adminEmail) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("email"), adminEmail);
    }

    public static Specification<Member> noBlockedMember(List<Long> blockedMemberIds) {
        return (root, query, cb) -> blockedMemberIds.isEmpty() ? null : cb.not(root.get("id").in(blockedMemberIds));
    }

    public static Specification<Member> spec(String occupation, String language, int career, String adminEmail, Member member, List<Long> blockedMemberIds) {
        return Specification
                .where(MemberSpecs.hasOccupation(occupation))
                .and(MemberSpecs.hasLanguage(language))
                .and(MemberSpecs.hasCareer(career))
                .and(MemberSpecs.notCurrentMember(member))
                .and(MemberSpecs.isNotDeleted())
                .and(MemberSpecs.isNicknameNotNull())
                .and(MemberSpecs.excludeAdminEmail(adminEmail))
                .and(MemberSpecs.noBlockedMember(blockedMemberIds));
    }
}

