package com.example.copro.auth.application;

import static com.example.copro.image.api.dto.response.DefaultImage.DEFAULT_IMAGE;

import com.example.copro.auth.api.dto.response.MemberLoginResDto;
import com.example.copro.auth.api.dto.response.UserInfo;
import com.example.copro.auth.exception.ExistsMemberEmailException;
import com.example.copro.auth.exception.NotFoundGithubEmailException;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.Role;
import com.example.copro.member.domain.SocialType;
import com.example.copro.member.domain.repository.MemberRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthMemberService {
    private final MemberRepository memberRepository;

    public AuthMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberLoginResDto saveUserInfo(UserInfo userInfo, SocialType provider) {
        validateNotFoundEmail(userInfo.email());

        Member member = getExistingMemberOrCreateNew(userInfo, provider);

        validateSocialType(member, provider);

        return MemberLoginResDto.from(member);
    }

    private void validateNotFoundEmail(String email) {
        if (email == null) {
            throw new NotFoundGithubEmailException();
        }
    }

    private Member getExistingMemberOrCreateNew(UserInfo userInfo, SocialType provider) {
        return memberRepository.findByEmail(userInfo.email()).orElseGet(() -> createMember(userInfo, provider));
    }

    private Member createMember(UserInfo userInfo, SocialType provider) {
        String userPicture = getUserPicture(userInfo.picture());

        return memberRepository.save(
                Member.builder()
                        .email(userInfo.email())
                        .name(userInfo.name())
                        .picture(userPicture)
                        .socialType(provider)
                        .role(Role.ROLE_USER)
                        .firstLogin(true)
                        .build()
        );
    }

    private String getUserPicture(String picture) {
        return Optional.ofNullable(picture)
                .map(this::convertToHighRes)
                .orElse(DEFAULT_IMAGE.imageUrl);
    }

    private String convertToHighRes(String url){
        return url.replace("s96-c", "s2048-c");
    }

    private void validateSocialType(Member member, SocialType provider) {
        if (!provider.equals(member.getSocialType())) {
            throw new ExistsMemberEmailException();
        }
    }

}
