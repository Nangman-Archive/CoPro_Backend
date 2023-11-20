package com.example.copro.member.application;

import com.example.copro.member.api.dto.respnse.MemberResDto;
import com.example.copro.member.api.dto.respnse.MembersResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MembersResDto memberInfoList() {
        List<Member> members = memberRepository.findAll();

        List<MemberResDto> memberResDtos = new ArrayList<>();
        for (Member member : members) {
            memberResDtos.add(MemberResDto.from(member));
        }

        return MembersResDto.from(memberResDtos);
    }

}
