package com.example.copro.board.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record ScrapReqDto (
        @NotNull
        Long boardId
){
}
