package com.example.copro.board.application;

import com.example.copro.board.api.dto.request.BoardSaveReqDto;
import com.example.copro.board.api.dto.request.HeartReqDto;
import com.example.copro.board.api.dto.request.ScrapReqDto;
import com.example.copro.board.api.dto.response.*;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.board.domain.MemberHeartBoard;
import com.example.copro.board.domain.repository.BoardRepository;
import com.example.copro.board.domain.repository.MemberHeartBoardRepository;
import com.example.copro.image.application.ImageService;
import com.example.copro.image.domain.Image;
import com.example.copro.image.domain.repository.ImageRepository;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberScrapBoard;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.domain.repository.MemberScrapBoardRepository;
import jakarta.persistence.NonUniqueResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MemberScrapBoardRepository memberScrapBoardRepository;

    private final MemberHeartBoardRepository memberHeartBoardRepository;

    private final ImageRepository imageRepository;

    private final @Lazy ImageService imageService; //순환참조 방지

//    @Value("${default-thumbnail-url}")
//    private String defaultThumbnailUrl;


    public BoardListRspDto findAll(Pageable pageable) { //페이지네이션 정보 담은 page객체 반환
//        Page<Board> pages = boardRepository.findAllWithImages(pageable);
//        for (Board board : pages) {
//            if (!board.getImages().isEmpty()) {
//                Image image = board.getImages().get(0);
////                board.setImageUrl(image.getImageUrl());
////                String imageUrl = image.getImageUrl();
//            } else {
////                board.setImageUrl(defaultThumbnailUrl);
////                String imageUrl = "기본Url";
//            }
//        }
//        return pages;
        Page<Board> boards = boardRepository.findAll(pageable);
        return BoardListRspDto.from(boards);
        /*return boardRepository.findAllWithMembersAndImages(pageable);*/
    }
//서비스에서 보드를 찾아 이미지가 null인지 아닌지
    @Transactional
    public BoardResDto createBoard(BoardSaveReqDto boardRequestDto) {
        Member member = memberRepository.findById(boardRequestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        // 이미지와 게시글 매핑 로직
        List<Image> images = imageRepository.findAllByIdIn(boardRequestDto.getImageId());

        // 이미지가 이미 매핑된 게시판이 있는지 체크하는 로직
        for (Image image : images) {
            Board existingBoard = boardRepository.findByImagesContaining(image);
            if (existingBoard != null) {
                throw new IllegalArgumentException(image.getId() + "번 이미지는 이미 매핑된 이미지입니다.");
            }
        }

        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .category(boardRequestDto.getCategory())
                .contents(boardRequestDto.getContents())
                .tag(boardRequestDto.getTag())
                .count(boardRequestDto.getCount())
                .heart(boardRequestDto.getHeart())
                .member(member)
                .images(images)
                .build();

        Board saveBoard = boardRepository.save(board);

        return BoardResDto.of(saveBoard);
    }

    @Transactional
    public BoardResDto updateBoard(Long boardId, BoardSaveReqDto boardSaveReqDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        // 이미지와 게시글 매핑 로직
        List<Image> images = imageRepository.findAllByIdIn(boardSaveReqDto.getImageId());

        // 이미지가 이미 매핑된 게시판이 있는지 체크하는 로직
        for (Image image : images) {
            Board existingBoard = boardRepository.findByImagesContaining(image);
            if (existingBoard != null) {
                throw new IllegalArgumentException(image.getId() + "번 이미지는 이미 매핑된 이미지입니다.");
            }
        }

        board.update(boardSaveReqDto,images);

        return BoardResDto.of(board);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(); // id로 이미지를 찾아서 반환
    }


    @Transactional
    public Page<Board> findByTitleContaining(String q, Pageable pageable) {
        return boardRepository.findByTitleContaining(q, pageable);
    }

    @Transactional
    public BoardResDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        board.updateViewCount(board.getCount());

        List<Long> heartMemberIds = memberHeartBoardRepository.findByBoardBoardId(boardId).stream()
                .map(MemberHeartBoard::getMember)
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        List<Long> scrapMemberIds = memberScrapBoardRepository.findByBoardBoardId(boardId).stream()
                .map(MemberScrapBoard::getMember)
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        return BoardResDto.from(board, heartMemberIds, scrapMemberIds);
    }

    public Category validateCategory(String category) {
        try {
            return Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 카테고리입니다.");
        }
    }

    @Transactional
    public ScrapSaveResDto scrapBoard(ScrapReqDto scrapSaveReqDto) {
        Board board = boardRepository.findById(scrapSaveReqDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다."));
        Member member = memberRepository.findById(scrapSaveReqDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다."));

        MemberScrapBoard memberScrapBoard = MemberScrapBoard.of(board, member);

        MemberScrapBoard saveScrap = memberScrapBoardRepository.save(memberScrapBoard);

        return ScrapSaveResDto.of(saveScrap);
    }

    @Transactional
    public void scrapDelete(ScrapReqDto scrapDeleteReqDto) {
        MemberScrapBoard memberScrapBoard = memberScrapBoardRepository.findByMemberMemberIdAndBoardBoardId(
                        scrapDeleteReqDto.getMemberId(), scrapDeleteReqDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("스크랩 정보가 존재하지 않습니다."));
        memberScrapBoardRepository.delete(memberScrapBoard);
    }

    @Transactional
    public HeartSaveResDto heartBoard(HeartReqDto heartSaveReqDto) {
        Board board = boardRepository.findById(heartSaveReqDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다."));
        Member member = memberRepository.findById(heartSaveReqDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다."));

        if(memberHeartBoardRepository.findByMemberMemberIdAndBoardBoardId(heartSaveReqDto.getMemberId(),heartSaveReqDto.getBoardId()).isPresent()){
            throw new NonUniqueResultException("이미 좋아요를 눌렀습니다.");
        }

        MemberHeartBoard memberHeartBoard = MemberHeartBoard.of(board,member);

        MemberHeartBoard saveLike = memberHeartBoardRepository.save(memberHeartBoard);
        board.updateHeartCount(board.getHeart());

        return HeartSaveResDto.of(saveLike);
    }

    @Transactional
    public void heartDelete(HeartReqDto heartDeleteReqDto) {
        MemberHeartBoard memberHeartBoard = memberHeartBoardRepository.findByMemberMemberIdAndBoardBoardId(
                        heartDeleteReqDto.getMemberId(), heartDeleteReqDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("좋아요 정보가 존재하지 않습니다."));
        Board board = boardRepository.findById(heartDeleteReqDto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다."));

        memberHeartBoardRepository.delete(memberHeartBoard);
        board.updateCancelHeartCount(board.getHeart());
    }

}
