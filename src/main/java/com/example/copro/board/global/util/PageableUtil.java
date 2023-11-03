package com.example.copro.board.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableUtil {

    /** 0-base인 페이지를 클라이언트단에서 1-based인 것처럼 사용할 수 있게 한다.
     * @param oneBasedPage
     * @param size
     * @return 0-based pageable Instance
     */
    public static Pageable of(int oneBasedPage, int size) {
        if (oneBasedPage < 1)
            throw new IllegalArgumentException("page는 1 이상이어야 합니다.");

        return PageRequest.of(oneBasedPage - 1 , size);
    }

    public static Pageable of(int oneBasedPage, int size, Sort sort) {
        if (oneBasedPage < 1)
            throw new IllegalArgumentException("page는 1 이상이어야 합니다.");

        return PageRequest.of(oneBasedPage - 1 , size, sort);
    }
}
