package com.example.copro.board.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass //테이블 생성 안됨. 해당 클래스 상속한 클래스에 db추가
@EntityListeners( value = { AuditingEntityListener.class } ) // JPA 내부에서 엔티티 객체가 생성/변경되는 것을 감지
@Getter
abstract class BaseTimeEntity {

    @CreatedDate //JPA에서 엔티티의 생성 시간을 처리
    @Column(name = "create_at")
    @Schema(description = "생성 시각", example = "1")
    private LocalDateTime createAt;

}
