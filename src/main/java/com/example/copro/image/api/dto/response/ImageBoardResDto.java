package com.example.copro.image.api.dto.response;


import com.example.copro.board.domain.Board;
import com.example.copro.image.domain.Image;
import lombok.Builder;

import java.util.List;

import static com.example.copro.image.api.dto.response.DefaultImage.*;

@Builder
public record ImageBoardResDto(
        String imageUrl
) {
  public static ImageBoardResDto from(Board board){
        List<Image> images = board.getImages();
        if(images.isEmpty()){
            return ImageBoardResDto.builder()
                    .imageUrl(DEFAULT_IMAGE.imageUrl)
                    .build();
        }

        return ImageBoardResDto.builder()
                .imageUrl(images.get(0).getImageUrl())
                .build();
    }
}
