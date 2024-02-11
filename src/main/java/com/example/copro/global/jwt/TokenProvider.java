package com.example.copro.global.jwt;

import com.example.copro.global.jwt.api.dto.TokenDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {
    private final MemberRepository memberRepository;

    @Value("${token.expire.time.access}")
    private String accessTokenExpireTime;

    @Value("${token.expire.time.refresh}")
    private String refreshTokenExpireTime;


    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] key = Decoders.BASE64URL.decode(secret);
        this.key = Keys.hmacShaKeyFor(key);
    }

    /*
     이 에러 쪽 어떻게 처리할지 생각해보기
     -> ControllerAdvice에서 처리 불가능
     -> 그 이유는 전역 예외처리 하기 전에 필터에서 이 예외를 가려내기 때문이다.
     -> 필터 실행 후 컨트롤러에서 함수 실행할 때 ControllerAdvice에서 예외 처리 가능.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            log.error("JWT is not valid");
        } catch (SignatureException exception) {
            log.error("JWT signature validation fails");
        } catch (ExpiredJwtException exception) {
            log.error("JWT expired"); // 만료 에러
        } catch (IllegalArgumentException exception) {
            log.error("JWT is null or empty or only whitespace");
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
        }

        return false;
    }

    // 엑세스, 리프레시토큰 생성
    public TokenDto createToken(String email) {
        String accessToken = generateAccessToken(email);
        String refreshToken = generateRefreshToken();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 만료된 엑세스토큰을 리프레시토큰으로 다시 재발급
    public TokenDto createAccessTokenByRefreshToken(String email, String refreshToken) {
        String accessToken = generateAccessToken(email);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 엑세스 토큰 생성
    public String generateAccessToken(String email) {
        Date date = new Date();
        Date accessExpiryDate = new Date(date.getTime() + Long.parseLong(accessTokenExpireTime));

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(date) // 발급 날짜
                .setExpiration(accessExpiryDate) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken() {
        Date date = new Date();
        Date refreshExpiryDate = new Date(date.getTime() + Long.parseLong(refreshTokenExpireTime));

        return Jwts.builder()
                .setExpiration(refreshExpiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // token을 받아서 토큰에 담긴 유저의 정보(Email)가 데이터베이스의 유저와 같은지 확인
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Member member = memberRepository.findByEmail(claims.getSubject()).orElseThrow();

        return new UsernamePasswordAuthenticationToken(member, "", Collections.emptyList());
    }
}
