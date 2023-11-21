package com.example.copro.member.application;

import com.example.copro.member.api.dto.request.MemberProfileUpdateReqDto;
import com.example.copro.member.api.dto.respnse.MemberResDto;
import com.example.copro.member.api.dto.respnse.MembersResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.exception.NotFoundMemberException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MembersResDto memberInfoList(String occupation, String language, String career) {
        Specification<Member> spec = Specification
                .where(MemberSpecs.hasOccupation(occupation))
                .and(MemberSpecs.hasLanguage(language))
                .and(MemberSpecs.hasCareer(career));

        List<Member> members = memberRepository.findAll(spec);

        List<MemberResDto> memberResDtos = new ArrayList<>();
        for (Member member : members) {
            memberResDtos.add(MemberResDto.from(member));
        }

        return MembersResDto.from(memberResDtos);
    }

    @Transactional
    public MemberResDto profileUpdate(Long memberId, MemberProfileUpdateReqDto memberProfileUpdateReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        member.profileUpdate(memberProfileUpdateReqDto);

        return MemberResDto.from(member);
    }
}
