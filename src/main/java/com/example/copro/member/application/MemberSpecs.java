package com.example.copro.member.application;

import com.example.copro.member.domain.Member;
import org.springframework.data.jpa.domain.Specification;

public class MemberSpecs {
    public static Specification<Member> hasOccupation(String occupation) {
        return (member, cq, cb) -> occupation == null ? null : cb.equal(member.get("occupation"), occupation);
    }

    public static Specification<Member> hasLanguage(String language) {
        return (member, cq, cb) -> language == null ? null : cb.equal(member.get("language"), language);
    }

    public static Specification<Member> hasCareer(String career) {
        return (member, cq, cb) -> career == null ? null : cb.equal(member.get("career"), career);
    }
}

