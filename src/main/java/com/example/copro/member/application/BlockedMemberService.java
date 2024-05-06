package com.example.copro.member.application;

import com.example.copro.member.api.dto.request.BlockedMemberReqDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.BlockedMemberMappingRepository;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.exception.ExistsBlockedMemberException;
import com.example.copro.member.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BlockedMemberService {
    private final BlockedMemberMappingRepository blockedMemberMappingRepository;
    private final MemberRepository memberRepository;

    public BlockedMemberService(BlockedMemberMappingRepository blockedMemberMappingRepository,
                                MemberRepository memberRepository) {
        this.blockedMemberMappingRepository = blockedMemberMappingRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void addBlockedMember(Member member, BlockedMemberReqDto blockedMemberReqDto) {
        Member getMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Member blockedMember = memberRepository.findByNickName(blockedMemberReqDto.blockedMemberNickName()).orElseThrow(MemberNotFoundException::new);

        validateExistsBlockedMember(getMember, blockedMember);

        getMember.addBlockedMember(blockedMember);
        memberRepository.save(getMember);
    }

    private void validateExistsBlockedMember(Member member, Member blockedMember) {
        if (blockedMemberMappingRepository.existsByMemberAndBlockedMember(member, blockedMember)) {
            throw new ExistsBlockedMemberException();
        }
    }

    @Transactional
    public void cancelBlockedMember(Member member, BlockedMemberReqDto blockedMemberReqDto) {
        Member getMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Member blockedMember = memberRepository.findByNickName(blockedMemberReqDto.blockedMemberNickName()).orElseThrow(MemberNotFoundException::new);

        getMember.cancelBlockedMember(blockedMember);
        memberRepository.save(getMember);
    }

}
