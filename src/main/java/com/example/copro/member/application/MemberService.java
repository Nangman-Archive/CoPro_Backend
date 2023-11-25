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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberLikeRepository memberLikeRepository;

    public MemberService(MemberRepository memberRepository, MemberLikeRepository memberLikeRepository) {
        this.memberRepository = memberRepository;
        this.memberLikeRepository = memberLikeRepository;
    }

    // 전체 멤버 정보리스트
    public Page<MemberResDto> memberInfoList(String occupation, String language, String career,
                                             int page, int size) {
        Page<Member> members = memberRepository.findAll(MemberSpecs.spec(occupation, language, career),
                PageRequest.of(page, size));

        return members.map(this::getMemberResDto);
    }

    private MemberResDto getMemberResDto(Member member) {
        List<Long> likeMembersId = new ArrayList<>();

        for (MemberLike memberLike : member.getMemberLikes()) {
            likeMembersId.add(memberLike.getLikedMember().getMemberId());
        }

        return MemberResDto.of(member, likeMembersId);
    }


    // 프로필 수정
    @Transactional
    public MemberResDto profileUpdate(Long memberId, MemberProfileUpdateReqDto memberProfileUpdateReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        member.profileUpdate(memberProfileUpdateReqDto);

        return MemberResDto.from(member);
    }

    // 좋아요 유저 목록
    public Page<MemberLikeResDto> memberLikeList(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        Page<MemberLike> memberLikes = memberLikeRepository.findByMember(member, PageRequest.of(page, size));

        return memberLikes.map(this::getMemberLikeResDto);
    }

    private MemberLikeResDto getMemberLikeResDto(MemberLike memberLike) {
        return MemberLikeResDto.from(memberLike);
    }

    // 유저 좋아요
    @Transactional
    public void addMemberLike(Long memberId, LikeMemberReqDto likeMemberReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        Member likeMember = memberRepository.findById(likeMemberReqDto.likeMemberId())
                .orElseThrow(NotFoundMemberException::new);

        validateExistsLikeMember(member, likeMember);

        member.addMemberLike(likeMember);
        memberRepository.save(member);
    }

    private void validateExistsLikeMember(Member member, Member likeMember) {
        if (memberLikeRepository.existsByMemberAndLikedMember(member, likeMember)) {
            throw new ExistsLikeMemberException();
        }
    }

    // 유저 좋아요 취소
    @Transactional
    public void cancelMemberLike(Long memberId, LikeMemberReqDto likeMemberReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        Member likeMember = memberRepository.findById(likeMemberReqDto.likeMemberId())
                .orElseThrow(NotFoundMemberException::new);

        member.cancelMemberLike(likeMember);
        memberRepository.save(member);
    }
}
