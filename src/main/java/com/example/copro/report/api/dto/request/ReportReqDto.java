package com.example.copro.report.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportReqDto (
        @NotNull
        Long boardId,
        @NotBlank
        String contents

){

}
