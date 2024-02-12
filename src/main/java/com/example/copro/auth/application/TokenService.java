package com.example.copro.auth.application;

import com.example.copro.auth.api.dto.request.RefreshTokenReqDto;
import com.example.copro.auth.api.dto.response.MemberLoginResDto;
import com.example.copro.auth.exception.InvalidTokenException;
import com.example.copro.global.jwt.TokenProvider;
import com.example.copro.global.jwt.api.dto.TokenDto;
import com.example.copro.global.jwt.domain.Token;
import com.example.copro.global.jwt.domain.repository.TokenRepository;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TokenService {

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    public TokenService(TokenProvider tokenProvider, TokenRepository tokenRepository, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.tokenRepository = tokenRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public TokenDto getToken(MemberLoginResDto memberLoginResDto) {
        TokenDto tokenDto = tokenProvider.generateToken(memberLoginResDto.findMember().getEmail());

        tokenSaveAndUpdate(memberLoginResDto, tokenDto);

        return tokenDto;
    }

    private void tokenSaveAndUpdate(MemberLoginResDto memberLoginResDto, TokenDto tokenDto) {
        if (!tokenRepository.existsByMember(memberLoginResDto.findMember())) {
            tokenRepository.save(Token.builder()
                    .member(memberLoginResDto.findMember())
                    .refreshToken(tokenDto.refreshToken())
                    .build());
        }

        refreshTokenUpdate(memberLoginResDto, tokenDto);
    }

    private void refreshTokenUpdate(MemberLoginResDto memberLoginResDto, TokenDto tokenDto) {
        Token token = tokenRepository.findByMember(memberLoginResDto.findMember()).orElseThrow();
        token.refreshTokenUpdate(tokenDto.refreshToken());
    }

    @Transactional
    public TokenDto generateAccessToken(RefreshTokenReqDto refreshTokenReqDto) {
        if (!tokenRepository.existsByRefreshToken(refreshTokenReqDto.refreshToken()) || !tokenProvider.validateToken(refreshTokenReqDto.refreshToken())) {
            throw new InvalidTokenException();
        }

        Token token = tokenRepository.findByRefreshToken(refreshTokenReqDto.refreshToken()).orElseThrow();
        Member member = memberRepository.findById(token.getMember().getMemberId()).orElseThrow(MemberNotFoundException::new);

        return tokenProvider.generateAccessTokenByRefreshToken(member.getEmail(), token.getRefreshToken());
    }

}
