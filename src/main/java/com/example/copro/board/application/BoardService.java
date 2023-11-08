package com.example.copro.board.application;

import com.example.copro.board.api.dto.request.BoardRequestDto;
import com.example.copro.board.api.dto.response.BoardResponseDto;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> findAll(Pageable pageable) { //페이지네이션 정보 담은 page객체 반환
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto) {
        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .category(boardRequestDto.getCategory())
                .contents(boardRequestDto.getContents())
                .tag(boardRequestDto.getTag())
                .count(boardRequestDto.getCount())
                .build();

        Board saveBoard = boardRepository.save(board);

        return BoardResponseDto.of(saveBoard);
    }

}
