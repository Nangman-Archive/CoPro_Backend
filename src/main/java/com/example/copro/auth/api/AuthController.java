package com.example.copro.auth.api;

import com.example.copro.auth.api.dto.request.RefreshTokenReqDto;
import com.example.copro.auth.api.dto.request.TokenReqDto;
import com.example.copro.auth.api.dto.response.MemberLoginResDto;
import com.example.copro.auth.api.dto.response.UserInfo;
import com.example.copro.auth.application.AuthMemberService;
import com.example.copro.auth.application.TokenService;
import com.example.copro.global.jwt.api.dto.TokenDto;
import com.example.copro.global.oauth.application.GitHubAuthService;
import com.example.copro.global.oauth.application.GoogleAuthService;
import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.domain.SocialType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final GoogleAuthService googleAuthService;
    private final GitHubAuthService gitHubAuthService;
    private final AuthMemberService memberService;
    private final TokenService tokenService;

    @Operation(summary = "로그인 후 토큰 발급", description = "액세스, 리프레쉬 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
    })
    @PostMapping("/{provider}/token")
    public RspTemplate<TokenDto> generateAccessAndRefreshToken(
            @Parameter(name = "provider", description = "소셜 타입(google, github, apple)", in = ParameterIn.PATH)
            @PathVariable(name = "provider") String provider,
            @RequestBody TokenReqDto tokenReqDto) {
        MemberLoginResDto getMemberDto = null;

        if (provider.equals("google")) {
            UserInfo userInfo = googleAuthService.getUserInfo(tokenReqDto.authCode());
            getMemberDto = memberService.saveUserInfo(userInfo, SocialType.GOOGLE);
        } else if (provider.equals("github")) {
            UserInfo userInfo = gitHubAuthService.getUserInfo(tokenReqDto.authCode());
            getMemberDto = memberService.saveUserInfo(userInfo, SocialType.GITHUB);
        }

        TokenDto getToken = tokenService.getToken(getMemberDto);

        return new RspTemplate<>(HttpStatus.OK, "토큰 발급", getToken);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "르프레쉬 토큰으로 액세스 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
    })
    @PostMapping("/token/access")
    public RspTemplate<TokenDto> generateAccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        TokenDto getToken = tokenService.generateAccessToken(refreshTokenReqDto);

        return new RspTemplate<>(HttpStatus.OK, "액세스 토큰 발급", getToken);
    }

}
