package com.example.copro.member.application;

import com.example.copro.member.domain.Member;
import jakarta.persistence.criteria.Predicate;
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

    public static Specification<Member> hasCareer(String career) {
        return (member, cq, cb) -> career == null ? null : cb.equal(member.get("career"), career);
    }

    public static Specification<Member> spec(String occupation, String language, String career) {
        return Specification
                .where(MemberSpecs.hasOccupation(occupation))
                .and(MemberSpecs.hasLanguage(language))
                .and(MemberSpecs.hasCareer(career));
    }
}

