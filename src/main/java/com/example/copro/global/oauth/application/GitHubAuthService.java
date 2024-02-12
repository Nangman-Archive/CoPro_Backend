package com.example.copro.global.oauth.application;

import com.example.copro.auth.api.dto.response.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
public class GitHubAuthService {

    @Value(value = "${oauth.github.client-id}")
    private String clientId;

    @Value(value = "${oauth.github.client-secret}")
    private String clientSecret;

    @Value(value = "${oauth.github.access-token-url}")
    private String getGithubAccessTokenUrl;

    @Value(value = "${oauth.github.user-info-url}")
    private String getGithubUserInfoUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public UserInfo getUserInfo(String code) {
        String accessToken = extractGithubAccessToken(code);
        UserInfo userInfo = requestGithubUserInfo(accessToken);

        return userInfo;
    }

    private String extractGithubAccessToken(String code) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = createGithubAccessTokenEntity(code);

        ResponseEntity<String> responseTokenEntity = restTemplate.exchange(
                getGithubAccessTokenUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);

        String[] tokenParts = responseTokenEntity.getBody().split("&");
        String[] accessTokenParts = tokenParts[0].split("=");
        return accessTokenParts[1];
    }

    private HttpEntity<MultiValueMap<String, String>> createGithubAccessTokenEntity(String code) {
        HttpHeaders httpHeaders = new HttpHeaders();

        MultiValueMap<String, String> reqParams = new LinkedMultiValueMap<>();
        reqParams.add("client_id", clientId);
        reqParams.add("client_secret", clientSecret);
        reqParams.add("code", code);

        return new HttpEntity<>(reqParams, httpHeaders);
    }

    private UserInfo requestGithubUserInfo(String accessToken) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = createRequestEntityWithAccessToken(accessToken);

        ResponseEntity<UserInfo> responseUserInfoEntity = restTemplate.exchange(
                getGithubUserInfoUrl,
                HttpMethod.GET,
                requestEntity,
                UserInfo.class);

        return responseUserInfoEntity.getBody();
    }

    private HttpEntity<MultiValueMap<String, String>> createRequestEntityWithAccessToken(String accessToken) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", String.format("token %s", accessToken));

        return new HttpEntity<>(requestHeaders);
    }

}
