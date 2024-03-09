package com.example.copro.board.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/hello")
@RestController
public class TestController {

    // 서버 이름을 기동 시에 매개변수로 전달하겠습니다
    @Value("${test.server.name}")
    private String serverName;

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello from " + serverName);
    }
}

