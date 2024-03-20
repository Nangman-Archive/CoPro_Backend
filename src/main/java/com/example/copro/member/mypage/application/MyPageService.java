package com.example.copro.member.mypage.application;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.repository.BoardRepository;
import com.example.copro.comment.api.dto.response.CommentResDto;
import com.example.copro.comment.domain.Comment;
import com.example.copro.comment.domain.repository.CommentRepository;
import com.example.copro.member.api.dto.response.MemberLikeResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberLike;
import com.example.copro.member.domain.MemberScrapBoard;
import com.example.copro.member.domain.repository.MemberLikeRepository;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.domain.repository.MemberScrapBoardRepository;
import com.example.copro.member.exception.MemberNotFoundException;
import com.example.copro.member.mypage.api.dto.request.UpdateViewTypeReqDto;
import com.example.copro.member.mypage.api.dto.response.DeleteAccountResDto;
import com.example.copro.member.mypage.api.dto.response.MyProfileInfoResDto;
import com.example.copro.member.mypage.api.dto.response.MyScrapBoardsResDto;
import com.example.copro.member.mypage.api.dto.response.MyWriteBoardResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MemberScrapBoardRepository memberScrapBoardRepository;
    private final MemberLikeRepository memberLikeRepository;
    private final CommentRepository commentRepository;

    // 본인 프로필 정보
    public MyProfileInfoResDto myProfileInfo(Member member) {
        Member getMember = memberRepository.findById(member.getMemberId()).orElseThrow();
        int likeMembersCount = memberLikeRepository.countByLikedMember(getMember);

        return MyProfileInfoResDto.myProfileInfoOf(member, likeMembersCount);
    }

    // 내 관심 프로필 목록
    public Page<MemberLikeResDto> memberLikeList(Member member, int page, int size) {
        Page<MemberLike> memberLikes = memberLikeRepository.findByMember(member, PageRequest.of(page, size));

        return memberLikes.map(memberLike -> mapToMemberLike(member, memberLike));
    }

    // 좋아요 여부와, 좋아요 횟수
    private MemberLikeResDto mapToMemberLike(Member member, MemberLike memberLike) {
        boolean isLike = memberLikeRepository.existsByMemberAndLikedMember(member, memberLike.getLikedMember());
        int likeMembersCount = memberLikeRepository.countByLikedMember(memberLike.getLikedMember());

        return MemberLikeResDto.of(memberLike, isLike, likeMembersCount);
    }

    // 내 관심 게시물 목록
    public Page<MyScrapBoardsResDto> boardLikeList(Member member, int page, int size) {
        Page<MemberScrapBoard> boards = memberScrapBoardRepository.findByMember(member, PageRequest.of(page, size));

        return boards.map(this::mapToMyScrapBoard);
    }

    private MyScrapBoardsResDto mapToMyScrapBoard(MemberScrapBoard memberScrapBoard) {
        int commentCount = commentRepository.countByBoardBoardId(memberScrapBoard.getBoard().getBoardId());

        return MyScrapBoardsResDto.of(memberScrapBoard, commentCount);
    }

    // 작성한 게시물 목록
    public Page<MyWriteBoardResDto> boardWriteList(Member member, int page, int size) {
        Page<Board> boards = boardRepository.findByMember(member, PageRequest.of(page, size));

        return boards.map(this::getMyWriteBoardResDto);
    }

    private MyWriteBoardResDto getMyWriteBoardResDto(Board board) {
        int commentCount = commentRepository.countByBoardBoardId(board.getBoardId());

        return MyWriteBoardResDto.of(board, commentCount);
    }

    // 작성 댓글
    public Page<CommentResDto> commentWriteList(Member member, int page, int size) {
        Page<Comment> myComments = commentRepository.findByWriter(member, PageRequest.of(page, size));

        return myComments.map(this::mapToMyComments);
    }

    private CommentResDto mapToMyComments(Comment comment) {
        return CommentResDto.from(comment);
    }

    // 뷰 타입 변경
    @Transactional
    public void updateViewType(Member member, UpdateViewTypeReqDto updateViewTypeReqDto) {
        Member getMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);
        getMember.viewTypeUpdate(updateViewTypeReqDto.viewType());
    }

    // 회원 탈퇴
    @Transactional
    public DeleteAccountResDto memberDeleteAccount(Member member) {
        DeleteAccountResDto responseEmail = DeleteAccountResDto.from(member);
        Member getMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);

        getMember.deleteAccount();
        return responseEmail;
    }

}
