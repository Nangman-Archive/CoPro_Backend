package com.example.copro.board.api;

import com.example.copro.board.api.dto.request.BoardSaveReqDto;
import com.example.copro.board.api.dto.request.HeartReqDto;
import com.example.copro.board.api.dto.request.ScrapReqDto;
import com.example.copro.board.api.dto.response.*;
import com.example.copro.board.application.BoardService;
import com.example.copro.board.application.ScheduledTasks;
import com.example.copro.board.domain.Category;
import com.example.copro.board.util.PageableUtil;
import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//서비스 클래스에서 예외처리하자 데이터, 메시지, 상태코드를 템플릿에 담아 보내라
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
@Tag(name = "board", description = "Board Controller")
public class BoardController {
    private final BoardService boardService;
    private final ScheduledTasks scheduledTasks;
    @Operation(summary = "게시물 조회", description = "전체 게시물 조회 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BoardListRspDto.class))),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @GetMapping("/list/{category}") //값 안보냈을때
    public RspTemplate<BoardListRspDto> handleGetAllBoard(
            @Parameter(name = "category", description = "게시물 카테고리", in = ParameterIn.PATH)
            @PathVariable("category") String category,
            @Parameter(name = "page", description = "게시물 page", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "1") int page,
            @Parameter(name = "standard", description = "정렬 기준 ex)create_at, count", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "create_at") String standard
    ) {
        // 내가 반환하고 싶은 것: 게시판Id, 제목 - BoardListRspDto
        Pageable pageable = null;
        // Pageable 객체의 구현체 PageRequest 가 필요하다
        final int DEFAULT_PAGE_SIZE = 7;

        if (standard.equals("create_at")) {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createAt"));
        } else if (standard.equals("count")) {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "count"));
        } else {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createAt"));
        }

        // Board List를 Service에서 가져온다.
        /*Page<Board> boardPage = boardService.findAll(pageable);*/

        // StudentListRspDto.from(students)를 통해 Dto의 리스트로 변환해서 반환한다.
        BoardListRspDto boardListRspDto = boardService.findAll(pageable);

        return new RspTemplate<>(HttpStatus.OK
                , page + "번 페이지 조회 완료"
                , boardListRspDto
        );
    }

    @Operation(summary = "게시물 등록", description = "게시물 등록 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = BoardResDto.class))),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping//게시글 등록
    public RspTemplate<BoardResDto> createBoard(@RequestBody BoardSaveReqDto boardRequestDto, @AuthenticationPrincipal Member member) {
        BoardResDto boardResDto = boardService.createBoard(boardRequestDto,member);
        return new RspTemplate<>(HttpStatus.OK
                , boardResDto.getBoardId() + "번 게시판 등록 완료"
                , boardResDto
        );
    }

    @Operation(summary = "게시물 수정", description = "게시물 수정 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BoardResDto.class))),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PutMapping //게시물 수정
    public RspTemplate<BoardResDto> updateBoard(@RequestParam("boardId") Long boardId,
                                                @RequestBody BoardSaveReqDto boardRequestDto, @AuthenticationPrincipal Member member) {
        BoardResDto boardResDto = boardService.updateBoard(boardId, boardRequestDto, member.getMemberId());
        return new RspTemplate<>(HttpStatus.OK
                , boardId + "번 게시물 수정 완료"
                , boardResDto);
    }

    @Operation(summary = "게시물 삭제", description = "게시물 삭제 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @DeleteMapping //게시물 삭제
    public RspTemplate<Void> deleteBoard(@RequestParam("boardId") Long boardId, @AuthenticationPrincipal Member member) {
        boardService.deleteBoard(boardId,member.getMemberId());
        return new RspTemplate<>(HttpStatus.OK, boardId + "번 게시물 삭제 완료");
    }

    @Operation(summary = "게시물 검색", description = "게시물 검색 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = BoardListRspDto.class))),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @GetMapping("/search") //게시글 검색(제목만)
    public RspTemplate<BoardListRspDto> searchBoard(
                                                    @Parameter(name = "q", description = "검색할 제목", in = ParameterIn.QUERY)
                                                    @RequestParam("q") String query,
                                                    @Parameter(name = "page", description = "게시물 page", in = ParameterIn.QUERY)
                                                    @RequestParam(defaultValue = "1") int page,
                                                    @Parameter(name = "standard", description = "정렬 기준 ex)create_at, count", in = ParameterIn.QUERY)
                                                    @RequestParam(defaultValue = "create_at") String standard) {
        // 내가 반환하고 싶은 것: 게시판Id, 제목 - BoardListRspDto
        Pageable pageable = null;
        // Pageable 객체의 구현체 PageRequest 가 필요하다
        final int DEFAULT_PAGE_SIZE = 7;

        if (standard.equals("create_at")) {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createAt"));
        } else if (standard.equals("count")) {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "count"));
        } else {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createAt"));
        }

        //검색어에 맞는 Board List를 Service에서 가져온다.
        //Page<Board> findBoardPage = boardService.findByTitleContaining(query, pageable);

        // StudentListRspDto.from(students)를 통해 Dto의 리스트로 변환해서 반환한다.
        //BoardListRspDto boardListRspDto = BoardListRspDto.from(findBoardPage);

        BoardListRspDto boardListRspDto = boardService.findByTitleContaining(query, pageable);

        return new RspTemplate<>(HttpStatus.OK
                , query + "조회 완료"
                , boardListRspDto
        );
    }

    @Operation(summary = "상세 페이지", description = "상세 페이지로 넘겨 줍니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BoardResDto.class))),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @GetMapping //상세 페이지
    public RspTemplate<BoardResDto> getBoard(@RequestParam("boardId") Long boardId) {
        BoardResDto boardResDto = boardService.getBoard(boardId);
        return new RspTemplate<>(HttpStatus.OK
                , boardId + "상세뷰 확인 완료"
                , boardResDto
        );
    }

    @Operation(summary = "작성 페이지 요청", description = "작성 페이지 요청 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @GetMapping("/{category}") //작성 페이지 요청
    public RspTemplate<String> showWritePage(@PathVariable("category") String category) {
        Category validCategory = boardService.validateCategory(category);
        return new RspTemplate<>(HttpStatus.OK, category + "조회 완료", category);
    }

    @Operation(summary = "스크랩 등록", description = "스크랩 등록 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/scrap/save")//스크랩 등록
    public RspTemplate<Void> scrapBoard(@RequestBody ScrapReqDto scrapSaveReqDto, @AuthenticationPrincipal Member member) {
        boardService.scrapBoard(scrapSaveReqDto,member.getMemberId());
        return new RspTemplate<>(HttpStatus.OK, scrapSaveReqDto.getBoardId() + "번 게시물 스크랩 완료");
    }

    @Operation(summary = "스크랩 삭제", description = "스크랩 삭제 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @DeleteMapping("/scrap")
    public RspTemplate<Void> scrapDelete(@RequestBody ScrapReqDto scrapDeleteReqDto, @AuthenticationPrincipal Member member) {
        boardService.scrapDelete(scrapDeleteReqDto, member.getMemberId());
        return new RspTemplate<>(HttpStatus.OK, scrapDeleteReqDto.getBoardId() + "번 게시물 스크랩 삭제 완료");
    }

    @Operation(summary = "좋아요 등록", description = "좋아요 등록 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/heart/save")
    public RspTemplate<HeartSaveResDto> heartBoard(@RequestBody HeartReqDto heartSaveReqDto, @AuthenticationPrincipal Member member) {
        boardService.heartBoard(heartSaveReqDto,member.getMemberId());
        return new RspTemplate<>(HttpStatus.OK, heartSaveReqDto.getBoardId() + "번 게시물 좋아요 완료");
    }

    @Operation(summary = "좋아요 삭제", description = "좋아요 삭제 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @DeleteMapping("/heart")
    public RspTemplate<Void> heartDelete(@RequestBody HeartReqDto heartDeleteReqDto, @AuthenticationPrincipal Member member) {
        boardService.heartDelete(heartDeleteReqDto,member.getMemberId());
        return new RspTemplate<>(HttpStatus.OK, heartDeleteReqDto.getBoardId() + "번 게시물 좋아요 삭제 완료");
    }

    @Operation(summary = "인기 게시물 조회", description = "인기 게시글 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @GetMapping("/most-increased-hearts")//인기 게시물
    public RspTemplate<BoardResDto> getMostIncreasedHeartsPost() {
        Long boardId = scheduledTasks.getMostIncreasedHeartsPostId();
        BoardResDto boardResDto = boardService.getBoard(boardId);
        return new RspTemplate<>(HttpStatus.OK
                , boardId + " 번 인기 게시물 조회 완료"
                , boardResDto
        );
    }

}
