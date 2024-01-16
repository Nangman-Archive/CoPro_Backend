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
import com.example.copro.board.exception.*;
import com.example.copro.comment.api.dto.response.CommentResDto;
import com.example.copro.comment.domain.repository.CommentRepository;
import com.example.copro.image.application.ImageService;
import com.example.copro.image.domain.Image;
import com.example.copro.image.domain.repository.ImageRepository;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberScrapBoard;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.domain.repository.MemberScrapBoardRepository;
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

    private final CommentRepository commentRepository;

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
    public BoardResDto createBoard(BoardSaveReqDto boardRequestDto,Member member) {
//        Member member = memberRepository.findById(member.getMemberId())
//                .orElseThrow(() -> new MemberNotFoundException(memberId));

        // 이미지와 게시글 매핑 로직
        List<Image> images = imageRepository.findAllByIdIn(boardRequestDto.getImageId());

        // 이미지가 이미 매핑된 게시판이 있는지 체크하는 로직
        for (Image image : images) {
            Board existingBoard = boardRepository.findByImagesContaining(image);
            if (existingBoard != null) {
                throw new MappedImageException(image);
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
    public BoardResDto updateBoard(Long boardId, BoardSaveReqDto boardSaveReqDto, Long memberId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        if (!board.getMember().getMemberId().equals(memberId)) {
            throw new NotOwnerException();
        }

        // 이미지와 게시글 매핑 로직
        List<Image> images = imageRepository.findAllByIdIn(boardSaveReqDto.getImageId());

        // 이미지가 이미 매핑된 게시판이 있는지 체크하는 로직
        for (Image image : images) {
            Board existingBoard = boardRepository.findByImagesContaining(image);
            if (existingBoard != null) {
                throw new MappedImageException(image);
            }
        }

        board.update(boardSaveReqDto,images);

        return BoardResDto.of(board);
    }

    @Transactional
    public void deleteBoard(Long boardId,Long memberId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        if (!board.getMember().getMemberId().equals(memberId)) {
            throw new NotOwnerException();
        }

        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId)); // id로 이미지를 찾아서 반환
    }

    @Transactional
    public BoardListRspDto findByTitleContaining(String q, Pageable pageable) {
        Page<Board> boards = boardRepository.findByTitleContaining(q, pageable);
        return BoardListRspDto.from(boards);
    }

    @Transactional
    public BoardResDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
        board.updateViewCount(board.getCount());

        List<Long> heartMemberIds = memberHeartBoardRepository.findByBoardBoardId(boardId).stream()
                .map(MemberHeartBoard::getMember)
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        List<Long> scrapMemberIds = memberScrapBoardRepository.findByBoardBoardId(boardId).stream()
                .map(MemberScrapBoard::getMember)
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        List<CommentResDto> commentResDtoList = commentRepository.findByBoardBoardId(boardId);

        return BoardResDto.from(board, heartMemberIds, scrapMemberIds, commentResDtoList);
    }

    public Category validateCategory(String category) {
        try {
            return Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new CategoryNotFoundException();
        }
    }

    @Transactional
    public void scrapBoard(ScrapReqDto scrapSaveReqDto,Long memberId) {
        Board board = boardRepository.findById(scrapSaveReqDto.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException(scrapSaveReqDto.getBoardId()));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        MemberScrapBoard memberScrapBoard = MemberScrapBoard.of(board, member);

        MemberScrapBoard saveScrap = memberScrapBoardRepository.save(memberScrapBoard);

        //return ScrapSaveResDto.of(saveScrap);
    }

    @Transactional
    public void scrapDelete(ScrapReqDto scrapDeleteReqDto, Long memberId) {
        MemberScrapBoard memberScrapBoard = memberScrapBoardRepository.findByMemberMemberIdAndBoardBoardId(
                        memberId, scrapDeleteReqDto.getBoardId())
                .orElseThrow(ScrapNotFoundException::new);
        memberScrapBoardRepository.delete(memberScrapBoard);
    }

    @Transactional
    public void heartBoard(HeartReqDto heartSaveReqDto, Long memberId) {
        Board board = boardRepository.findById(heartSaveReqDto.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException(heartSaveReqDto.getBoardId()));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        if(memberHeartBoardRepository.findByMemberMemberIdAndBoardBoardId(memberId,heartSaveReqDto.getBoardId()).isPresent()){
            throw new AlreadyHeartException();
        }

        MemberHeartBoard memberHeartBoard = MemberHeartBoard.of(board,member);

        MemberHeartBoard saveLike = memberHeartBoardRepository.save(memberHeartBoard);
        board.updateHeartCount(board.getHeart());

        //return HeartSaveResDto.of(saveLike);
    }

    @Transactional
    public void heartDelete(HeartReqDto heartDeleteReqDto, Long memberId) {
        MemberHeartBoard memberHeartBoard = memberHeartBoardRepository.findByMemberMemberIdAndBoardBoardId(
                        memberId, heartDeleteReqDto.getBoardId())
                .orElseThrow(HeartNotFoundException::new);
        Board board = boardRepository.findById(heartDeleteReqDto.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException(heartDeleteReqDto.getBoardId()));

        memberHeartBoardRepository.delete(memberHeartBoard);
        board.updateCancelHeartCount(board.getHeart());
    }

}
