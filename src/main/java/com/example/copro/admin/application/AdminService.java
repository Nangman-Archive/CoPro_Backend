package com.example.copro.admin.application;

import com.example.copro.admin.api.dto.request.NoticeSaveReqDto;
import com.example.copro.admin.api.dto.response.ReportInfoResDto;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.board.domain.repository.BoardRepository;
import com.example.copro.board.exception.BoardNotFoundException;
import com.example.copro.comment.domain.repository.CommentRepository;
import com.example.copro.member.api.dto.response.MemberResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.notification.domain.repository.NotificationRepository;
import com.example.copro.report.domain.Report;
import com.example.copro.report.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final ReportRepository reportRepository;

    // 유저 정보 조회
    public Page<MemberResDto> membersInfo(Member member, int page, int size) {
        Page<Member> members = memberRepository.findAll(PageRequest.of(page, size));

        return members.map(MemberResDto::from);
    }

    // 공지사항 작성
    @Transactional
    public void createNotice(Member member, NoticeSaveReqDto noticeSaveReqDto) {
        boardRepository.save(Board.builder()
                .title(noticeSaveReqDto.title())
                .category(Category.valueOf("공지사항"))
                .contents(noticeSaveReqDto.contents())
                .member(member)
                .build());
    }

    // 공지사항 수정
    @Transactional
    public void updateNotice(Member member, Long boardId, NoticeSaveReqDto noticeSaveReqDto) {
        Board notice = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        notice.noticeUpdate(noticeSaveReqDto);
    }

    // 공지사항, 자유, 프로젝트 삭제
    @Transactional
    public void deleteBoard(Member member, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        commentRepository.deleteByBoardBoardId(boardId);
        notificationRepository.deleteByBoardId(boardId);

        boardRepository.delete(board);
    }

    // 신고 내역 리스트 조회
    public Page<ReportInfoResDto> reports(Member member, int page, int size) {
        Page<Report> reports = reportRepository.findAll(PageRequest.of(page, size));

        return reports.map(this::getReportResDto);
    }

    private ReportInfoResDto getReportResDto(Report report) {
        return ReportInfoResDto.of(report.getMember(), report.getBoard(), report);
    }

}
