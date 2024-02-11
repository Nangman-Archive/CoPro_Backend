package com.example.copro.auth.application;

import com.example.copro.auth.api.dto.response.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final ObjectMapper objectMapper;

    public AuthService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // decode한 payload로 사용자 정보 찾기
    @Transactional
    public UserInfo getUserInfo(String idToken) {
        String decodePayload = getDecodePayload(idToken);

        try {
            return objectMapper.readValue(decodePayload, UserInfo.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage()); // 커스텀에러작성
        }
    }

    // payload를 decode하기
    private String getDecodePayload(String idToken) {
        String payload = getPayload(idToken);

        return new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
    }

    // Id토큰을 -> payload
    private String getPayload(String idToken) {
        return idToken.split("\\.")[1];
    }





}
