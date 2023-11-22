package com.example.copro.member.application;

import com.example.copro.member.api.dto.request.LikeMemberReqDto;
import com.example.copro.member.api.dto.request.MemberProfileUpdateReqDto;
import com.example.copro.member.api.dto.respnse.MemberLikeResDto;
import com.example.copro.member.api.dto.respnse.MemberResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberLike;
import com.example.copro.member.domain.repository.MemberLikeRepository;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.exception.ExistsLikeMemberException;
import com.example.copro.member.exception.NotFoundMemberException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberLikeRepository memberLikeRepository;

    public MemberService(MemberRepository memberRepository, MemberLikeRepository memberLikeRepository) {
        this.memberRepository = memberRepository;
        this.memberLikeRepository = memberLikeRepository;
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

    public List<MemberLikeResDto> memberLikeList(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        List<MemberLikeResDto> memberLikeResDtos = new ArrayList<>();
        for (MemberLike memberLike : member.getMemberLikes()) {
            memberLikeResDtos.add(MemberLikeResDto.from(memberLike));
        }

        return memberLikeResDtos;
    }

    @Transactional
    public void addMemberLike(Long memberId, LikeMemberReqDto likeMemberReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        Member likeMember = memberRepository.findById(likeMemberReqDto.likeMemberId())
                .orElseThrow(NotFoundMemberException::new);

        if (memberLikeRepository.existsByMemberAndLikedMember(member, likeMember)) {
            throw new ExistsLikeMemberException();
        }

        member.addMemberLike(likeMember);
        memberRepository.save(member);
    }

    @Transactional
    public void cancelMemberLike(Long memberId, LikeMemberReqDto likeMemberReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        Member likeMember = memberRepository.findById(likeMemberReqDto.likeMemberId())
                .orElseThrow(NotFoundMemberException::new);

        member.cancelMemberLike(likeMember);
        memberRepository.save(member);
    }
}
