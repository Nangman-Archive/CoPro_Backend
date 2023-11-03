package com.example.copro.board.api.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PageInfoDto {

    int currentPage; // 현재 페이지 번호
    int size;               // 페이지당 기본 사이즈
    boolean hasNext;    // 다음 페이지 존재 여부
    boolean hasPrevious;   // 이전 페이지 존재 여부
    boolean isFirst;   // 첫번째 페이지 여부
    boolean isLast;   // 마지막 페이지 여부
    int numberOfElements;  // 현재 페이지의 데이터 수

    long totalElements; // 전체 데이터 수
    int totalPages; // 전체 페이지 수

    public static PageInfoDto from(Page<?> page) { //Page 객체를 PageInfoDto 객체로 변환
        return PageInfoDto.builder()
                .currentPage(page.getNumber() + 1) // zero-based index이므로 1을 더해줌
                .size(page.getSize())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
