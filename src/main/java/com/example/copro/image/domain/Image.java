package com.example.copro.image.domain;

import com.example.copro.board.domain.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "이미지 id", example = "1")
    @Column(name = "image_id", nullable = false)
    private Long id;

    @Column(name = "image_url", nullable = false)
    @Schema(description = "이미지 url", example = "https://dddd")
    private String imageUrl;

    @Column(name = "convert_image_name", nullable = false)
    @Schema(description = "변환된 이미지 파일 이름", example = "35324cfc-e04a-4559-beab-f41a9c23d759_KakaoTalk_20231030_161509818.jpg")
    private String convertImageName;

    @Builder
    private Image(String imageUrl, String convertImageName, Board board) {
        this.imageUrl = imageUrl;
        this.convertImageName = convertImageName;
    }

}
