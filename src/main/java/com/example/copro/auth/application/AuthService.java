package com.example.copro.auth.application;

import com.example.copro.auth.api.dto.response.UserInfo;

public interface AuthService {
    UserInfo getUserInfo(String authCode);

    String getProvider();
}
