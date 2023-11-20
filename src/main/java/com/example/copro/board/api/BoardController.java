package com.example.copro.board.api;

import com.example.copro.board.api.common.RspTemplate;
import com.example.copro.board.api.dto.request.BoardSaveReqDto;
import com.example.copro.board.api.dto.request.ReportReqDto;
import com.example.copro.board.api.dto.request.ScrapReqDto;
import com.example.copro.board.api.dto.response.BoardListRspDto;
import com.example.copro.board.api.dto.response.BoardResDto;
import com.example.copro.board.api.dto.response.ReportResDto;
import com.example.copro.board.api.dto.response.ScrapSaveResDto;
import com.example.copro.board.application.BoardService;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.board.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

//서비스 클래스에서 예외처리하자 데이터, 메시지, 상태코드를 템플릿에 담아 보내라
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list/{category}") //값 안보냈을때
    public RspTemplate<BoardListRspDto> handleGetAllBoard(
            @PathVariable("category") String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "create_at") String standard
    ) {
        // 내가 반환하고 싶은 것: 게시판Id, 제목 - BoardListRspDto
        Pageable pageable = null;
        // Pageable 객체의 구현체 PageRequest 가 필요하다
        final int DEFAULT_PAGE_SIZE = 10;

        if(standard.equals("create_at")){
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createAt"));
        } else if (standard.equals("count")) {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "count"));
        } else {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createAt"));
        }

        // Board List를 Service에서 가져온다.
        Page<Board> boardPage = boardService.findAll(pageable);

        // StudentListRspDto.from(students)를 통해 Dto의 리스트로 변환해서 반환한다.
        BoardListRspDto boardListRspDto = BoardListRspDto.from(boardPage);

        return new RspTemplate<>(HttpStatus.OK
                , page + "번 페이지 조회 완료"
                , boardListRspDto
        );
    }

    @PostMapping//게시글 등록
    public RspTemplate<BoardResDto> createBoard(@RequestBody BoardSaveReqDto boardRequestDto) {
        BoardResDto boardResDto = boardService.createBoard(boardRequestDto);
        return new RspTemplate<>(HttpStatus.OK
                , boardResDto.getBoardId() + "번 게시판 조회 완료"
                , boardResDto
        );
    }

    @PutMapping //게시물 수정
    public RspTemplate<BoardResDto> updateBoard(@RequestParam("boardId") Long boardId, @RequestBody BoardSaveReqDto boardRequestDto){
        BoardResDto boardResDto = boardService.updateBoard(boardId, boardRequestDto);
        return new RspTemplate<>(HttpStatus.OK
        ,boardId + "번 게시물 수정 완료"
        , boardResDto);
    }

    @DeleteMapping //게시물 삭제
    public RspTemplate<Void> deleteBoard(@RequestParam("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return new RspTemplate<>(HttpStatus.OK, boardId + "번 게시물 삭제 완료");
    }

    @GetMapping("/search") //게시글 검색(제목만)
    public RspTemplate<BoardListRspDto> searchBoard(@RequestParam("q") String query, @RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "create_at") String standard)
    {
        // 내가 반환하고 싶은 것: 게시판Id, 제목 - BoardListRspDto
        Pageable pageable = null;
        // Pageable 객체의 구현체 PageRequest 가 필요하다
        final int DEFAULT_PAGE_SIZE = 10;

        if(standard.equals("create_at")){
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createAt"));
        } else if (standard.equals("count")) {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "count"));
        } else {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createAt"));
        }

        //검색어에 맞는 Board List를 Service에서 가져온다.
        Page<Board> findBoardPage = boardService.findByTitleContaining(query, pageable);

        // StudentListRspDto.from(students)를 통해 Dto의 리스트로 변환해서 반환한다.
        BoardListRspDto boardListRspDto = BoardListRspDto.from(findBoardPage);

        return new RspTemplate<>(HttpStatus.OK
                , query + "조회 완료"
                , boardListRspDto
        );
    }

    @GetMapping //상세 페이지
    public RspTemplate<BoardResDto> getBoard(@RequestParam("boardId") Long boardId) {
        BoardResDto boardResDto = boardService.getBoard(boardId);
        return new RspTemplate<>(HttpStatus.OK
                , boardId + "상세뷰 확인 완료"
                , boardResDto
        );
    }

    @GetMapping("/{category}") //작성 페이지 요청
    public RspTemplate<String> showWritePage(@PathVariable("category") String category) {
        Category validCategory = boardService.validateCategory(category);
        return new RspTemplate<>(HttpStatus.OK,category + "조회 완료", category);
    }

    @PostMapping("/report") //게시글 신고
    public RspTemplate<ReportResDto> reportBoard(@RequestBody ReportReqDto reportReqDto){
        ReportResDto reportResDto = boardService.reportBoard(reportReqDto);
        return new RspTemplate<>(HttpStatus.OK, reportResDto.getBoardId() + "신고 완료", reportResDto);
    }

    @PostMapping("/scrap/save")//스크랩 등록
    public RspTemplate<ScrapSaveResDto> scrapBoard(@RequestBody ScrapReqDto scrapSaveReqDto){
        ScrapSaveResDto scrapSaveResDto = boardService.scrapBoard(scrapSaveReqDto);
        return new RspTemplate<>(HttpStatus.OK, scrapSaveResDto.getBoardId() + "번 게시물 스크랩 완료", scrapSaveResDto);
    }

    @DeleteMapping("/scrap/delete")
    public RspTemplate<Void> scrapDelete(@RequestBody ScrapReqDto scrapDeleteReqDto) {
        boardService.scrapDelete(scrapDeleteReqDto);
        return new RspTemplate<>(HttpStatus.OK, scrapDeleteReqDto.getBoardId() + "번 게시물 스크랩 삭제 완료");
    }

}
