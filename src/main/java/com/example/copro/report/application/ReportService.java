package com.example.copro.report.application;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.repository.BoardRepository;
import com.example.copro.board.exception.BoardNotFoundException;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.exception.MemberNotFoundException;
import com.example.copro.report.api.dto.request.ReportReqDto;
import com.example.copro.report.domain.Report;
import com.example.copro.report.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public void reportBoard(ReportReqDto reportReqDto, Long memberId) {
        Board board = boardRepository.findById(reportReqDto.getBoardId()).orElseThrow(() -> new BoardNotFoundException(reportReqDto.getBoardId()));
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        Report report = Report.builder()
                .board(board)
                .member(member)
                .contents(reportReqDto.getContents())
                .build();

        reportRepository.save(report);
    }
}
