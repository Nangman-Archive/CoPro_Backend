package com.example.copro.board.application;

import com.example.copro.board.api.dto.request.BoardSaveReqDto;
import com.example.copro.board.api.dto.request.ReportReqDto;
import com.example.copro.board.api.dto.request.ScrapReqDto;
import com.example.copro.board.api.dto.response.BoardResDto;
import com.example.copro.board.api.dto.response.ReportResDto;
import com.example.copro.board.api.dto.response.ScrapSaveResDto;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.board.domain.Report;
import com.example.copro.board.domain.repository.BoardRepository;
import com.example.copro.board.domain.repository.ReportRepository;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberScrapBoard;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.domain.repository.MemberScrapBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    private final ReportRepository reportRepository;

    private final MemberScrapBoardRepository memberScrapBoardRepository;

    @Transactional
    public Page<Board> findAll(Pageable pageable) { //페이지네이션 정보 담은 page객체 반환
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public BoardResDto createBoard(BoardSaveReqDto boardRequestDto) {
        Member member = memberRepository.findById(boardRequestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .category(boardRequestDto.getCategory())
                .contents(boardRequestDto.getContents())
                .tag(boardRequestDto.getTag())
                .count(boardRequestDto.getCount())
                .member(member)
                .build();

        Board saveBoard = boardRepository.save(board);

        return BoardResDto.of(saveBoard);
    }

    @Transactional
    public BoardResDto updateBoard(Long boardId, BoardSaveReqDto boardSaveReqDto){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        board.update(boardSaveReqDto.getTitle(),boardSaveReqDto.getCategory(), boardSaveReqDto.getContents(), boardSaveReqDto.getTag());

        return BoardResDto.of(board);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        boardRepository.delete(board);
    }

    @Transactional
    public Page<Board> findByTitleContaining(String q, Pageable pageable) {
        return boardRepository.findByTitleContaining(q, pageable);
    }

    @Transactional
    public BoardResDto getBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        board.updateViewCount(board.getCount());
        return BoardResDto.of(board);
    }

    public Category validateCategory(String category) {
        try {
            return Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 카테고리입니다.");
        }
    }

    @Transactional
    public ReportResDto reportBoard(ReportReqDto reportReqDto) {
        Board board = boardRepository.findById(reportReqDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다."));
        Member member = memberRepository.findById(reportReqDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다."));

        Report report = Report.builder()
                .board(board)
                .member(member)
                .contents(reportReqDto.getContents())
                .build();

        Report saveReport = reportRepository.save(report);

        return ReportResDto.of(saveReport);
    }

    @Transactional
    public ScrapSaveResDto scrapBoard(ScrapReqDto scrapSaveReqDto) {
        Board board = boardRepository.findById(scrapSaveReqDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다."));
        Member member = memberRepository.findById(scrapSaveReqDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다."));

        MemberScrapBoard memberScrapBoard = MemberScrapBoard.builder()
                .board(board)
                .member(member)
                .build();

        MemberScrapBoard saveScrap = memberScrapBoardRepository.save(memberScrapBoard);

        return ScrapSaveResDto.of(saveScrap);
    }

    @Transactional
    public void scrapDelete(ScrapReqDto scrapDeleteReqDto) {
        MemberScrapBoard memberScrapBoard = memberScrapBoardRepository.findByMemberMemberIdAndBoardBoardId(scrapDeleteReqDto.getMemberId(),scrapDeleteReqDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("스크랩 정보가 존재하지 않습니다."));
        memberScrapBoardRepository.delete(memberScrapBoard);
    }

}
