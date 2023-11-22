package com.example.copro.member.application;

import com.example.copro.member.api.dto.request.MemberProfileUpdateReqDto;
import com.example.copro.member.api.dto.respnse.MemberResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.exception.NotFoundMemberException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Page<MemberResDto> memberInfoList(String occupation, String language, String career,
                                             int page, int size) {
        Page<Member> members = memberRepository.findAll(MemberSpecs.spec(occupation, language, career),
                PageRequest.of(page, size));

        return members.map(MemberResDto::from);
    }

    @Transactional
    public MemberResDto profileUpdate(Long memberId, MemberProfileUpdateReqDto memberProfileUpdateReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        member.profileUpdate(memberProfileUpdateReqDto);

        return MemberResDto.from(member);
    }
}
