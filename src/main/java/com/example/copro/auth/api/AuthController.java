package com.example.copro.auth.api;

import com.example.copro.auth.api.dto.request.IdTokenReqDto;
import com.example.copro.auth.api.dto.request.RefreshTokenReqDto;
import com.example.copro.auth.api.dto.response.MemberLoginResDto;
import com.example.copro.auth.api.dto.response.UserInfo;
import com.example.copro.auth.application.AuthMemberService;
import com.example.copro.auth.application.AuthService;
import com.example.copro.auth.application.TokenService;
import com.example.copro.global.jwt.TokenProvider;
import com.example.copro.global.jwt.api.dto.TokenDto;
import com.example.copro.global.jwt.domain.Token;
import com.example.copro.global.jwt.domain.repository.TokenRepository;
import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthController {
    private final TokenProvider tokenProvider;

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    private final AuthService authService;
    private final AuthMemberService memberService;
    private final TokenService tokenService;

    // 프론트에서 ID토큰만 줄 때
    @PostMapping("/{provider}/id-token")
    public ResponseEntity<TokenDto> generateTokenByIdToken(@PathVariable(name = "provider") String provider,
                                                           @RequestBody IdTokenReqDto idTokenReqDto) {

        // idToken을 이용해서 유저 정보 가져오기.
        UserInfo userInfo = authService.getUserInfo(idTokenReqDto.idToken());

        // 유저가 존재하지 않으면 디비에 저장 후 memberDTO 반환
        MemberLoginResDto getMemberDto = memberService.saveUserInfo(userInfo, "google");

        // member email로 엑세스 토큰, 리프레시 토큰 발급.
        TokenDto getToken = tokenService.getToken(getMemberDto);

        // 엑세스토큰, 리프레시토큰 반환
        return new ResponseEntity<>(getToken, HttpStatus.OK);
    }

    @PostMapping("/access")
    public RspTemplate<TokenDto> createAccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        // 리프레시 토큰이 이미 디비에 존재한다,  리프레시 토큰이 만료되지 않았다. -> 리프레시 토큰 살아있으므로 엑세스 토큰만 받아서 반환.
        if (tokenRepository.existsByRefreshToken(refreshTokenReqDto.refreshToken()) && tokenProvider.validateToken(refreshTokenReqDto.refreshToken())) {
            Token token = tokenRepository.findByRefreshToken(refreshTokenReqDto.refreshToken()).orElseThrow();
            Member member = memberRepository.findById(token.getMember().getMemberId()).orElseThrow();

            TokenDto accessTokenByRefreshToken = tokenProvider.createAccessTokenByRefreshToken(member.getEmail(), token.getRefreshToken());

            return new RspTemplate<>(HttpStatus.OK, "엑세스 토큰 발급 성공", accessTokenByRefreshToken);
        }

        // 리프레시 토큰이 만료되었으니 다시 로그인
        return new RspTemplate<>(HttpStatus.UNAUTHORIZED, "재로그인 해주세요.");
    }

    @GetMapping("/info")
    public ResponseEntity<String> userInfo(@AuthenticationPrincipal Member member) {
        return new ResponseEntity<>("memberEmail : " + member.getEmail(), HttpStatus.OK);
    }

}
