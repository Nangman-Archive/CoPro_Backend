package com.example.copro.member.mypage.application;

import com.example.copro.board.api.dto.response.BoardListRspDto;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.repository.BoardRepository;
import com.example.copro.member.api.dto.request.UpdateViewTypeReqDto;
import com.example.copro.member.api.dto.response.MemberLikeResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberLike;
import com.example.copro.member.domain.MemberScrapBoard;
import com.example.copro.member.domain.repository.MemberLikeRepository;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.domain.repository.MemberScrapBoardRepository;
import com.example.copro.member.exception.NotFoundMemberException;
import com.example.copro.member.mypage.api.dto.response.MyProfileInfoResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final MemberScrapBoardRepository memberScrapBoardRepository;
    private final MemberLikeRepository memberLikeRepository;

    // 본인 프로필 정보
    public MyProfileInfoResDto myProfileInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        int likeMembersCount = member.getMemberLikes().size();

        return MyProfileInfoResDto.myProfileInfoOf(member,likeMembersCount);
    }

    // 내 관심 프로필 목록
    public Page<MemberLikeResDto> memberLikeList(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        Page<MemberLike> memberLikes = memberLikeRepository.findByMember(member, PageRequest.of(page, size));

        return memberLikes.map(this::mapToMemberLike);
    }

    private MemberLikeResDto mapToMemberLike(MemberLike memberLike) {
        return MemberLikeResDto.from(memberLike);
    }

    // 내 관심 게시물 목록
    public BoardListRspDto boardLikeList(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        Page<MemberScrapBoard> boards = memberScrapBoardRepository.findByMember(member, PageRequest.of(page, size));

        return BoardListRspDto.memberScrapBoardFrom(boards);
    }

    // 작성한 게시물 목록
    public BoardListRspDto boardWriteList(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        Page<Board> boards = boardRepository.findByMember(member, PageRequest.of(page,size));

        return BoardListRspDto.from(boards);
    }

    // 작성 댓글

    // 뷰 타입 변경
    @Transactional
    public void updateViewType(Long memberId, UpdateViewTypeReqDto updateViewTypeReqDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundMemberException::new);

        member.viewTypeUpdate(updateViewTypeReqDto.viewType());
    }
}
