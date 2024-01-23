package com.example.copro.board.application;

import com.example.copro.board.api.dto.request.BoardSaveReqDto;
import com.example.copro.board.api.dto.request.HeartReqDto;
import com.example.copro.board.api.dto.request.ScrapReqDto;
import com.example.copro.board.api.dto.response.BoardDto;
import com.example.copro.board.api.dto.response.BoardListRspDto;
import com.example.copro.board.api.dto.response.BoardResDto;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.board.domain.MemberHeartBoard;
import com.example.copro.board.domain.repository.BoardRepository;
import com.example.copro.board.domain.repository.MemberHeartBoardRepository;
import com.example.copro.board.exception.AlreadyHeartException;
import com.example.copro.board.exception.AlreadyScrapException;
import com.example.copro.board.exception.BoardNotFoundException;
import com.example.copro.board.exception.HeartNotFoundException;
import com.example.copro.board.exception.MappedImageException;
import com.example.copro.board.exception.NotOwnerException;
import com.example.copro.board.exception.ScrapNotFoundException;
import com.example.copro.comment.api.dto.response.CommentResDto;
import com.example.copro.comment.domain.repository.CommentRepository;
import com.example.copro.image.domain.Image;
import com.example.copro.image.domain.repository.ImageRepository;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.domain.repository.MemberScrapBoardRepository;
import com.example.copro.member.exception.MemberNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MemberScrapBoardRepository memberScrapBoardRepository;
    private final MemberHeartBoardRepository memberHeartBoardRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;

    public BoardListRspDto findAll(String category, Pageable pageable) {
        //Page<Board> boards = boardRepository.findAllByCategory(Category.valueOf(category), pageable);
        Page<BoardDto> boards = boardRepository.findAllWithCommentCount(category, pageable);

        return BoardListRspDto.of(boards);
    }

    //서비스에서 보드를 찾아 이미지가 null인지 아닌지
    @Transactional
    public BoardResDto createBoard(BoardSaveReqDto boardSaveReqDto, Member member) {
        // 이미지와 게시글 매핑 로직
        List<Image> images = imageRepository.findAllByIdIn(boardSaveReqDto.imageId());

        // 이미지가 이미 매핑된 게시판이 있는지 체크하는 로직
        checkForAlreadyMappedImages(images);

        Board board = builderBoard(boardSaveReqDto, member, images);
        Board saveBoard = boardRepository.save(board);

        return BoardResDto.of(saveBoard);
    }

    private Board builderBoard(BoardSaveReqDto boardSaveReqDto, Member member, List<Image> images) {
        return Board.builder()
                .title(boardSaveReqDto.title())
                .category(boardSaveReqDto.category())
                .contents(boardSaveReqDto.contents())
                .tag(boardSaveReqDto.tag())
                .count(boardSaveReqDto.count())
                .heart(boardSaveReqDto.heart())
                .member(member)
                .images(images)
                .build();
    }

    @Transactional
    public BoardResDto updateBoard(Long boardId, BoardSaveReqDto boardSaveReqDto, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));

        checkBoardOwnership(board, member);

        // 이미지와 게시글 매핑 로직
        List<Image> images = imageRepository.findAllByIdIn(boardSaveReqDto.imageId());

        // 이미지가 이미 매핑된 게시판이 있는지 체크하는 로직
        checkForAlreadyMappedImages(images);

        board.update(boardSaveReqDto, images);

        return BoardResDto.of(board);
    }

    // 이미지가 이미 매핑된 게시판이 있는지 체크하는 로직
    private void checkForAlreadyMappedImages(List<Image> images) {
        for (Image image : images) {
            Board existingBoard = boardRepository.findByImagesContaining(image);

            if (existingBoard != null) {
                throw new MappedImageException(image);
            }
        }
    }

    @Transactional
    public void deleteBoard(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        checkBoardOwnership(board, member);

        boardRepository.delete(board);
    }

    // member가 board의 소유자가 아닐 경우 예외처리
    private void checkBoardOwnership(Board board, Member member) {
        if (!board.getMember().getMemberId().equals(member.getMemberId())) {
            throw new NotOwnerException();
        }
    }

    public BoardListRspDto findByTitleContaining(String query, Pageable pageable) {
        Page<BoardDto> boards = boardRepository.findByTitleContaining(query, pageable);
        return BoardListRspDto.of(boards);
    }

    // 상세 게시판
    public BoardResDto getBoard(Member member, Long boardId) {
        Member getMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
        board.updateViewCount();

        boolean isHeart = memberHeartBoardRepository.existsByMemberAndBoard(getMember, board);
        boolean isScrap = memberScrapBoardRepository.existsByMemberAndBoard(getMember, board);

        List<CommentResDto> commentResDtoList = commentRepository.findByBoardBoardId(boardId);

<<<<<<< HEAD
        return BoardResDto.from(board, heartMemberIds, scrapMemberIds, commentResDtoList);
    }

    private List<Long> getHeartMemberIds(Board board) {
        return memberHeartBoardRepository.findByBoardBoardId(board.getBoardId()).stream()
                .map(MemberHeartBoard::getMember)
                .map(Member::getMemberId)
                .collect(Collectors.toList());
    }

    private List<Long> getScrapMemberIds(Board board) {
        return memberScrapBoardRepository.findByBoardBoardId(board.getBoardId()).stream()
                .map(MemberScrapBoard::getMember)
                .map(Member::getMemberId)
                .collect(Collectors.toList());
=======
        return BoardResDto.from(board, isHeart, isScrap,commentResDtoList);
>>>>>>> d8d8be79813cc51b220527ef69b8dcb12809a1de
    }

    @Transactional
    public void scrapBoard(ScrapReqDto scrapSaveReqDto, Member member) {
        Board board = boardRepository.findById(scrapSaveReqDto.boardId()).orElseThrow(() -> new BoardNotFoundException(scrapSaveReqDto.boardId()));
        Member addScrapMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);

        validateScrapNotExists(addScrapMember, board);

        addScrapMember.addScrapBoard(board);
        memberRepository.save(addScrapMember);
    }

    private void validateScrapNotExists(Member addScrapMember, Board board) {
        if(memberScrapBoardRepository.findByMemberMemberIdAndBoardBoardId(addScrapMember.getMemberId(), board.getBoardId()).isPresent()) {
            throw new AlreadyScrapException();
        }
    }

    @Transactional
    public void scrapDelete(ScrapReqDto scrapDeleteReqDto, Member member) {
        Board board = boardRepository.findById(scrapDeleteReqDto.boardId()).orElseThrow(() -> new BoardNotFoundException(scrapDeleteReqDto.boardId()));
        Member deleteScrapMember = memberRepository.findById(member.getMemberId()).orElseThrow(MemberNotFoundException::new);

        validateScrapNotFound(deleteScrapMember, board);

        deleteScrapMember.cancelScrapBoard(board);
        memberRepository.save(deleteScrapMember);
    }

    private void validateScrapNotFound(Member deleteScrapMember, Board board) {
        if (memberScrapBoardRepository.findByMemberMemberIdAndBoardBoardId(deleteScrapMember.getMemberId(), board.getBoardId()).isEmpty()) {
            throw new ScrapNotFoundException();
        }
    }

    @Transactional
    public void heartBoard(HeartReqDto heartSaveReqDto, Member member) {
        Board board = boardRepository.findById(heartSaveReqDto.boardId())
                .orElseThrow(() -> new BoardNotFoundException(heartSaveReqDto.boardId()));

        validateHeartNotExists(member, heartSaveReqDto);

        MemberHeartBoard memberHeartBoard = MemberHeartBoard.of(board, member);

        board.updateHeartCount();
        memberHeartBoardRepository.save(memberHeartBoard);
    }

    private void validateHeartNotExists(Member member, HeartReqDto heartSaveReqDto) {
        if (memberHeartBoardRepository.findByMemberMemberIdAndBoardBoardId(member.getMemberId(), heartSaveReqDto.boardId()).isPresent()) {
            throw new AlreadyHeartException();
        }
    }

    @Transactional
    public void heartDelete(HeartReqDto heartDeleteReqDto, Member member) {
        MemberHeartBoard memberHeartBoard = memberHeartBoardRepository.findByMemberMemberIdAndBoardBoardId(member.getMemberId(), heartDeleteReqDto.boardId())
                .orElseThrow(HeartNotFoundException::new);
        Board board = boardRepository.findById(heartDeleteReqDto.boardId())
                .orElseThrow(() -> new BoardNotFoundException(heartDeleteReqDto.boardId()));

        board.updateCancelHeartCount();
        memberHeartBoardRepository.delete(memberHeartBoard);
    }
}
