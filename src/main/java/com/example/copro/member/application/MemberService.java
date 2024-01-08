package com.example.copro.member.application;

import com.example.copro.member.api.dto.request.MemberLikeReqDto;
import com.example.copro.member.api.dto.request.MemberProfileUpdateReqDto;
import com.example.copro.member.api.dto.response.MemberChattingProfileResDto;
import com.example.copro.member.api.dto.response.MemberInfoResDto;
import com.example.copro.member.api.dto.response.MemberResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberLikeRepository;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.exception.ExistsLikeMemberException;
import com.example.copro.member.exception.InvalidMemberException;
import com.example.copro.member.exception.NotFoundMemberException;
import java.util.List;
import java.util.Optional;
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

    // nickname으로 member채팅프로필 불러오기
    public MemberChattingProfileResDto memberChattingProProfileInfo(String nickName) {
        Member member = memberRepository.findByNickName(nickName).orElseThrow(NotFoundMemberException::new);

        return MemberChattingProfileResDto.from(member);
    }

    // 전체 멤버 정보리스트
    public MemberInfoResDto memberInfoList(Long memberId, String occupation, String language, String career, int page, int size) {
        String o = Optional.ofNullable(occupation).map(String::trim).filter(s -> !s.isEmpty()).orElse(null);
        String l = Optional.ofNullable(language).map(String::trim).filter(s -> !s.isEmpty()).orElse(null);
        String c = Optional.ofNullable(career).map(String::trim).filter(s -> !s.isEmpty()).orElse(null);

        Page<Member> members = memberRepository.findAll(MemberSpecs.spec(o, l, c), PageRequest.of(page, size));

        return MemberInfoResDto.of(getViewType(memberId), members.map(this::getMemberResDto));
    }

    private MemberResDto getMemberResDto(Member member) {
        List<Long> likeMembersId = member.getMemberLikes().stream()
                .map(memberLike -> memberLike.getLikedMember().getMemberId())
                .toList();

        int likeMembersCount = member.getMemberLikes().size();

        return MemberResDto.of(member, likeMembersCount, likeMembersId);
    }

    private int getViewType(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        return member.getViewType();
    }

    // 프로필 수정
    @Transactional
    public MemberResDto profileUpdate(Long memberId, MemberProfileUpdateReqDto memberProfileUpdateReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        validateDuplicateNickName(memberProfileUpdateReqDto.nickName());
        member.profileUpdate(memberProfileUpdateReqDto);

        return MemberResDto.from(member);
    }

    // nickName 중복검사
    private void validateDuplicateNickName(String nickName) {
        if (memberRepository.existsByNickName(nickName)) {
            throw new InvalidMemberException("이미 존재하는 닉네임입니다.");
        }
    }

    // 유저 좋아요
    @Transactional
    public void addMemberLike(Long memberId, MemberLikeReqDto memberLikeReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        Member likeMember = memberRepository.findById(memberLikeReqDto.likeMemberId())
                .orElseThrow(NotFoundMemberException::new);

        validateExistsLikeMember(member, likeMember);

        member.addMemberLike(likeMember);
        memberRepository.save(member);
    }

    // member 관심 목록 중복검사
    private void validateExistsLikeMember(Member member, Member likeMember) {
        if (memberLikeRepository.existsByMemberAndLikedMember(member, likeMember)) {
            throw new ExistsLikeMemberException();
        }
    }

    // 유저 좋아요 취소
    @Transactional
    public void cancelMemberLike(Long memberId, MemberLikeReqDto memberLikeReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);
        Member likeMember = memberRepository.findById(memberLikeReqDto.likeMemberId())
                .orElseThrow(NotFoundMemberException::new);

        member.cancelMemberLike(likeMember);
        memberRepository.save(member);
    }
}
