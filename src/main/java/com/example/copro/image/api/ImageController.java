package com.example.copro.image.api;

import com.example.copro.board.application.BoardService;
import com.example.copro.global.template.RspTemplate;
import com.example.copro.image.api.dto.request.ImageReqDto;
import com.example.copro.image.api.dto.request.ImageUrlReqDto;
import com.example.copro.image.api.dto.response.ImageResDto;
import com.example.copro.image.application.ImageService;
import com.example.copro.image.domain.Image;
import com.example.copro.member.application.MemberService;
import com.example.copro.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ImageController {
    private final ImageService imageService;
    private final MemberService memberService;

    public ImageController(ImageService imageService, MemberService memberService) {
        this.imageService = imageService;
        this.memberService = memberService;
    }

    @Operation(summary = "프로필 이미지 업로드", description = "이미지 업로드 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping(value = "/images/profile", consumes = "multipart/form-data")
    public RspTemplate<ImageResDto> imageUpload(@RequestPart("image") MultipartFile image, @AuthenticationPrincipal Member member) {
        String folderName = "profile";
        ImageResDto upload = imageService.upload(folderName,image);

        memberService.updateProfileImage(member.getMemberId(), upload.imageUrl());

        return new RspTemplate<>(HttpStatus.OK, "업로드 완료",upload);
    }

    @Operation(summary = "프로필 이미지 삭제", description = "이미지 삭제 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ImageUrlReqDto.class))),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping ("/images/profile/delete")
    public RspTemplate<Void> deleteImage(@RequestBody ImageUrlReqDto imageUrlReqDto) {

        imageService.deleteImageByUrl(imageUrlReqDto);

        return new RspTemplate<>(HttpStatus.OK, "이미지 삭제 완료");
    }

    @Operation(summary = "게시판 이미지 삭제", description = "이미지 삭제 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ImageReqDto.class))),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/images/delete")///boards/{boardId}
    public RspTemplate<Void> deleteImage(@RequestParam(required = false) Long boardId, @RequestBody ImageReqDto imageReqDto) {

        // 게시글과 이미지의 연관 관계 제거 및 이미지 삭제
        imageService.delete(boardId, imageReqDto.imageIds());

        return new RspTemplate<>(HttpStatus.OK, "이미지 삭제 완료");
    }

    @Operation(summary = "게시판 이미지 조회", description = "이미지 조회 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @GetMapping("/images/{imageId}")
    public RspTemplate<ImageResDto> getImage(@PathVariable Long imageId) {
        Image image = imageService.findById(imageId);
        return new RspTemplate<>(HttpStatus.OK, "이미지 조회 완료", ImageResDto.from(image));
    }

    @Operation(summary = "게시판 이미지 업로드", description = "이미지 업로드 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/images/board")
    public RspTemplate<List<ImageResDto>> uploadMultiple(@RequestPart("files") MultipartFile[] files) {
        String folderName = "board";
        List<ImageResDto> upload = imageService.uploadMultiple(folderName, files);
        return new RspTemplate<>(HttpStatus.OK, "업로드 완료",upload);
    }

}
