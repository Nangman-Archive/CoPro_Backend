package com.example.copro.image.domain;

import com.example.copro.board.domain.Board;
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
    @Column(name = "image_id", nullable = false)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "convert_image_name", nullable = false)
    private String convertImageName;

    @Builder
    private Image(String imageUrl, String convertImageName, Board board) {
        this.imageUrl = imageUrl;
        this.convertImageName = convertImageName;
    }

}
