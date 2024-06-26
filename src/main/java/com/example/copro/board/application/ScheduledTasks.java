package com.example.copro.board.application;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.repository.BoardRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ScheduledTasks {
    private final BoardRepository boardRepository;

    public ScheduledTasks(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    private static Long mostIncreasedHeartBoardId;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    //@Scheduled(cron = "*/60 * * * * *") // 매 1초마다 실행
    public void findMostIncreasedHeartBoard() {
        List<Board> results = boardRepository.findWithMaxIncreaseInHeart();
        if (results.isEmpty()) {
            throw new NoSuchElementException("게시물이 없습니다.");
        } else {
            Board mostIncreasedHeartBoard = results.get(0);
            mostIncreasedHeartBoardId = mostIncreasedHeartBoard.getBoardId();
        }

        // 이전 좋아요 수 갱신
        updatePreviousHeartCounts();
    }

    private void updatePreviousHeartCounts() {
        List<Board> allBoards = boardRepository.findAll();
        for (Board board : allBoards) {
            board.setPreviousHeartCount(board.getHeart());
        }
        boardRepository.saveAll(allBoards);
    }

    public Long getMostIncreasedHeartsPostId() {
        return mostIncreasedHeartBoardId;
    }
}