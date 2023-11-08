package com.example.copro.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing// 생성일과 수정일을 자동으로 추적하고 업데이트
public class JpaBoardConfig {

}
