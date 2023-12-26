package com.example.copro.image.api;

import com.example.copro.board.application.BoardService;
import com.example.copro.board.domain.Board;
import com.example.copro.global.template.RspTemplate;
import com.example.copro.image.api.dto.response.ImageResDto;
import com.example.copro.image.application.ImageService;
import com.example.copro.image.domain.Image;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ImageController {
    private final ImageService imageService;
    private final BoardService boardService;

    public ImageController(ImageService imageService, BoardService boardService) {
        this.imageService = imageService;
        this.boardService = boardService;
    }

    @PostMapping(value = "/images", consumes = "multipart/form-data")
    public RspTemplate<ImageResDto> imageUpload(@RequestPart("image") MultipartFile image) {
        ImageResDto upload = imageService.upload(image);

        return new RspTemplate<>(HttpStatus.OK, "업로드 완료",upload);
    }

    @DeleteMapping("/boards/{boardId}/images/{imageId}")
    public RspTemplate<Void> deleteImage(@PathVariable Long boardId, @PathVariable Long imageId) {
        // 게시글 찾기
        //Board board = boardService.findById(boardId);

        // 이미지 찾기
        //Image image = imageService.findById(imageId);

        // 게시글과 이미지의 연관 관계 제거 및 이미지 삭제
        imageService.delete(boardId, imageId);

        return new RspTemplate<>(HttpStatus.OK, "이미지 삭제 완료");
    }

    @GetMapping("/images/{imageId}")
    public RspTemplate<ImageResDto> getImage(@PathVariable Long imageId) {
        Image image = imageService.findById(imageId);
        return new RspTemplate<>(HttpStatus.OK, "이미지 조회 완료", ImageResDto.of(image));
    }

    @PostMapping("/imagess")
    public RspTemplate<List<ImageResDto>> uploadMultiple(@RequestPart("files") MultipartFile[] files) {
        List<ImageResDto> upload = imageService.uploadMultiple(files);
        return new RspTemplate<>(HttpStatus.OK, "업로드 완료",upload);
    }

}
