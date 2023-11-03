package com.example.copro.board.application;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.repository.BoardRepository;
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

    public Page<Board> findAll(Pageable pageable) {
        // List<Student>
        // Page<Student>
        return boardRepository.findAll(pageable);
    }

}
