package com.example.copro.board.api;

import com.example.copro.board.api.common.RspTemplate;
import com.example.copro.board.api.dto.request.BoardRequestDto;
import com.example.copro.board.api.dto.response.BoardListRspDto;
import com.example.copro.board.api.dto.response.BoardResponseDto;
import com.example.copro.board.application.BoardService;
import com.example.copro.board.domain.Board;
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
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "create_at"));
        } else if (standard.equals("create_at_asc")) {
            pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.ASC, "create_at"));
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

    @PostMapping("/save")//게시글 등록                           boardsavereqdto
    public RspTemplate<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto boardResponseDto = boardService.createBoard(boardRequestDto);
        return new RspTemplate<>(HttpStatus.OK
                , boardResponseDto.getBoardId() + "번 게시판 조회 완료"
                , boardResponseDto
        );
    }

}
