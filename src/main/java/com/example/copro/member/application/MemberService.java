package com.example.copro.member.application;

import com.example.copro.member.api.dto.request.MemberGitHubUrlUpdateReqDto;
import com.example.copro.member.api.dto.request.MemberLikeReqDto;
import com.example.copro.member.api.dto.request.MemberProfileUpdateReqDto;
import com.example.copro.member.api.dto.response.MemberInfoResDto;
import com.example.copro.member.api.dto.response.MemberResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.BlockedMemberMappingRepository;
import com.example.copro.member.domain.repository.MemberLikeRepository;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.exception.ExistsLikeMemberException;
import com.example.copro.member.exception.ExistsNickNameException;
import com.example.copro.member.exception.MemberNotFoundException;
import com.example.copro.notification.application.FCMNotificationService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    @Value("${admin.email}")
    private String adminEmail;

    private final MemberRepository memberRepository;
    private final MemberLikeRepository memberLikeRepository;
    private final BlockedMemberMappingRepository blockedMemberMappingRepository;
    private final FCMNotificationService fcmNotificationService;

    // nickname으로 member채팅프로필 불러오기
    public MemberResDto memberChattingProfileInfo(Member member, String targetEmail) {
        Member targetMember = memberRepository.findByEmail(targetEmail).orElseThrow(MemberNotFoundException::new);

        return getMemberResDto(member, targetMember);
    }

    // 전체 멤버 정보리스트
    public MemberInfoResDto memberInfoList(Member member, String occupation, String language, int career, int page, int size) {
        String o = Optional.ofNullable(occupation).map(String::trim).filter(s -> !s.isEmpty()).orElse(null);
        String l = Optional.ofNullable(language).map(String::trim).filter(s -> !s.isEmpty()).orElse(null);

        List<Long> blockedMemberIds = blockedMemberMappingRepository.findByBlockedMemberId(member);

        Page<Member> members = memberRepository.findAll(MemberSpecs.spec(o, l, career, adminEmail, member, blockedMemberIds), PageRequest.of(page, size));

        return MemberInfoResDto.of(getViewType(member), members.map(currentMember -> getMemberResDto(member, currentMember)));
    }

    private MemberResDto getMemberResDto(Member member, Member currentMember) {
        boolean isLike = memberLikeRepository.existsByMemberAndLikedMember(member, currentMember);
        int likeMembersCount = memberLikeRepository.countByLikedMember(currentMember);

        return MemberResDto.of(currentMember, likeMembersCount, isLike);
    }

    private int getViewType(Member member) {
        return member.getViewType();
    }

    // 프로필 수정
    @Transactional
    public MemberResDto profileUpdate(Member member, MemberProfileUpdateReqDto memberProfileUpdateReqDto) {
        Member getMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);

        validateDuplicateMyNickName(memberProfileUpdateReqDto.nickName(), getMember);
        getMember.profileUpdate(memberProfileUpdateReqDto);

        return MemberResDto.from(member);
    }

    private void validateDuplicateMyNickName(String nickName, Member member) {
        if (memberRepository.existsByNickName(nickName) && member.getNickName() != null && !member.getNickName().equals(nickName)) {
            throw new ExistsNickNameException();
        }
    }

    // nickName 중복검사
    public void validateDuplicateNickName(String nickName) {
        if (memberRepository.existsByNickName(nickName)) {
            throw new ExistsNickNameException();
        }
    }

    // gitHubUrl 수정
    @Transactional
    public MemberResDto gitHubUrlUpdate(Member member, MemberGitHubUrlUpdateReqDto memberGitHubUrlUpdateReqDto) {
        Member getMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);
        getMember.gitHubUrlUpdate(memberGitHubUrlUpdateReqDto);
        getMember.firstLongUpdate();

        return MemberResDto.from(member);
    }

    // 유저 좋아요
    @Transactional
    public void addMemberLike(Member member, MemberLikeReqDto memberLikeReqDto) {
        Member getMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Member likeMember = memberRepository.findById(memberLikeReqDto.likeMemberId()).orElseThrow(MemberNotFoundException::new);

        validateExistsLikeMember(getMember, likeMember);

        getMember.addMemberLike(likeMember);
        memberRepository.save(getMember);

        fcmNotificationService.sendLikeMemberNotification(getMember, likeMember);
    }

    // member 관심 목록 중복검사
    private void validateExistsLikeMember(Member member, Member likeMember) {
        if (memberLikeRepository.existsByMemberAndLikedMember(member, likeMember)) {
            throw new ExistsLikeMemberException();
        }
    }

    // 유저 좋아요 취소
    @Transactional
    public void cancelMemberLike(Member member, MemberLikeReqDto memberLikeReqDto) {
        Member getMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Member likeMember = memberRepository.findById(memberLikeReqDto.likeMemberId())
                .orElseThrow(MemberNotFoundException::new);

        getMember.cancelMemberLike(likeMember);
        memberRepository.save(getMember);
    }

    @Transactional
    public void updateProfileImage(Long memberId, String imageUrl) {
        // 멤버를 찾아옵니다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버를 찾을 수 없습니다."));

        // 멤버의 프로필 이미지 URL을 업데이트합니다.
        member.updateImage(imageUrl);

        // 변경사항을 저장합니다.
        memberRepository.save(member);
    }
}
