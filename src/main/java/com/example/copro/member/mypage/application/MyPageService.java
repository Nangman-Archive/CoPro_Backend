package com.example.copro.member.mypage.application;

import com.example.copro.board.api.dto.response.BoardListRspDto;
import com.example.copro.member.api.dto.response.MemberLikeResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberLike;
import com.example.copro.member.domain.MemberScrapBoard;
import com.example.copro.member.domain.repository.MemberLikeRepository;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.domain.repository.MemberScrapBoardRepository;
import com.example.copro.member.exception.NotFoundMemberException;
import com.example.copro.member.mypage.api.dto.response.MyProfileInfoResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MyPageService {
    private final MemberRepository memberRepository;
    private final MemberScrapBoardRepository memberScrapBoardRepository;
    private final MemberLikeRepository memberLikeRepository;

    public MyPageService(MemberRepository memberRepository, MemberScrapBoardRepository memberScrapBoardRepository,
                         MemberLikeRepository memberLikeRepository) {
        this.memberRepository = memberRepository;
        this.memberScrapBoardRepository = memberScrapBoardRepository;
        this.memberLikeRepository = memberLikeRepository;
    }

    // 본인 프로필 정보
    public MyProfileInfoResDto myProfileInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        return MyProfileInfoResDto.myProfileInfoFrom(member);
    }

    // 좋아요 유저 목록
    public Page<MemberLikeResDto> memberLikeList(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        Page<MemberLike> memberLikes = memberLikeRepository.findByMember(member, PageRequest.of(page, size));

        return memberLikes.map(this::mapToMemberLike);
    }

    private MemberLikeResDto mapToMemberLike(MemberLike memberLike) {
        return MemberLikeResDto.from(memberLike);
    }

    // 작성한 게시물 목록


    // 관심 게시물 목록
    public BoardListRspDto boardLikeList(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        Page<MemberScrapBoard> boards = memberScrapBoardRepository.findByMember(member, PageRequest.of(page, size));

        return BoardListRspDto.memberScrapBoardFrom(boards);
    }

    // 작성 댓글

}
