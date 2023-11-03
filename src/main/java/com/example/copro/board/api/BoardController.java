package com.example.copro.board.api;

import com.example.copro.board.api.common.RspTemplate;
import com.example.copro.board.api.dto.response.BoardListRspDto;
import com.example.copro.board.application.BoardService;
import com.example.copro.board.domain.Board;
import com.example.copro.board.global.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//서비스 클래스에서 예외처리하자 데이터, 메시지, 상태코드를 템플릿에 담아 보내라
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/board")
    public RspTemplate<BoardListRspDto> handleGetAllStudents(
            @RequestParam(defaultValue = "1") int page
    ) {
        // 내가 반환하고 싶은 것: 게시판Id, 제목 - BoardListRspDto

        // Pageable 객체의 구현체 PageRequest 가 필요하다
        final int DEFAULT_PAGE_SIZE = 10;
        Pageable pageable = PageableUtil.of(page, DEFAULT_PAGE_SIZE);

        // Student List를 Service에서 가져온다.
        Page<Board> studentPage = boardService.findAll(pageable);
        // StudentListRspDto.from(students)를 통해 Dto의 리스트로 변환해서 반환한다.
        BoardListRspDto studentListRspDto = BoardListRspDto.from(studentPage);

        return new RspTemplate<>(HttpStatus.OK
                , page + "번 페이지 조회 완료"
                , studentListRspDto
        );
    }

}
