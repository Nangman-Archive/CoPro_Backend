package com.example.copro.auth.api;

import com.example.copro.auth.api.dto.request.IdTokenReqDto;
import com.example.copro.auth.api.dto.request.RefreshTokenReqDto;
import com.example.copro.auth.api.dto.response.MemberLoginResDto;
import com.example.copro.auth.api.dto.response.UserInfo;
import com.example.copro.auth.application.AuthMemberService;
import com.example.copro.auth.application.AuthService;
import com.example.copro.auth.application.TokenService;
import com.example.copro.global.jwt.api.dto.TokenDto;
import com.example.copro.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
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
    private final AuthService authService;
    private final AuthMemberService memberService;
    private final TokenService tokenService;

    @Operation(summary = "토큰 발급", description = "액세스, 리프레쉬 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
    })
    @PostMapping("/{provider}/token")
    public RspTemplate<TokenDto> generateAccessAndRefreshToken(@PathVariable(name = "provider") String provider, @RequestBody IdTokenReqDto idTokenReqDto) {
        UserInfo userInfo = authService.getUserInfo(idTokenReqDto.idToken());
        MemberLoginResDto getMemberDto = memberService.saveUserInfo(userInfo, "google");
        TokenDto getToken = tokenService.getToken(getMemberDto);

        return new RspTemplate<>(HttpStatus.OK, "토큰 발급" ,getToken);
    }

    @Operation(summary = "액세스 토큰 발급", description = "액세스 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
    })
    @PostMapping("/token/access")
    public RspTemplate<TokenDto> generateAccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        TokenDto getToken = tokenService.generateAccessToken(refreshTokenReqDto);

        return new RspTemplate<>(HttpStatus.OK, "액세스 토큰 발급", getToken);
    }

}
