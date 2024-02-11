package com.example.copro.auth.application;

import com.example.copro.auth.api.dto.response.MemberLoginResDto;
import com.example.copro.global.jwt.TokenProvider;
import com.example.copro.global.jwt.api.dto.TokenDto;
import com.example.copro.global.jwt.domain.Token;
import com.example.copro.global.jwt.domain.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TokenService {

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    public TokenService(TokenProvider tokenProvider, TokenRepository tokenRepository) {
        this.tokenProvider = tokenProvider;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public TokenDto getToken(MemberLoginResDto memberLoginResDto) {
        // member email로 엑세스 토큰, 리프레시 토큰 발급.
        TokenDto tokenDto = tokenProvider.createToken(memberLoginResDto.findMember().getEmail());

        // 해당 유저의 토큰이 존재하지 않으면, 해당 유저의 토큰 디비 생성
        // 유저로 토큰 디비 찾아서 로그인 할 때 마다 리프레시토큰 업데이트, 하지만 이미 같은 토큰이면 업데이트 안함.
        tokenSaveAndUpdate(memberLoginResDto, tokenDto);

        return tokenDto;
    }

    private void tokenSaveAndUpdate(MemberLoginResDto memberLoginResDto, TokenDto tokenDto) {
        // 해당 유저의 토큰이 존재하지 않으면, 해당 유저의 토큰 디비 생성
        if (!tokenRepository.existsByMember(memberLoginResDto.findMember())) {
            tokenRepository.save(Token.builder()
                    .member(memberLoginResDto.findMember())
                    .refreshToken(tokenDto.refreshToken())
                    .build());
        }

        refreshTokenUpdate(memberLoginResDto, tokenDto);
    }

    private void refreshTokenUpdate(MemberLoginResDto memberLoginResDto, TokenDto tokenDto) {
        // 유저로 토큰 디비 찾아서 로그인 할 때 마다 리프레시토큰 업데이트,
        // 하지만 이미 같은 토큰이면 업데이트 안함.
        Token token = tokenRepository.findByMember(memberLoginResDto.findMember()).orElseThrow();
        token.refreshTokenUpdate(tokenDto.refreshToken());
    }

}
