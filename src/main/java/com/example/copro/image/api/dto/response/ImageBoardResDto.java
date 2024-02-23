package com.example.copro.image.api.dto.response;


import com.example.copro.board.domain.Board;
import com.example.copro.image.domain.Image;
import java.util.List;
import lombok.Builder;

@Builder
public record ImageBoardResDto(
        String imageUrl
) {
  public static ImageBoardResDto from(Board board){
        List<Image> images = board.getImages();
        if(images.isEmpty()){
            return ImageBoardResDto.builder()
                    .imageUrl(null)
                    .build();
        }

        return ImageBoardResDto.builder()
                .imageUrl(images.get(0).getImageUrl())
                .build();
    }
}
