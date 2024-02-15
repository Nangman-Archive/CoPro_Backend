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
        validateEmail(userInfo.email());

        String userPicture = getUserPicture(userInfo.picture());

        Member member = memberRepository.save(
                Member.builder()
                        .email(userInfo.email())
                        .name(userInfo.name())
                        .picture(userPicture)
                        .socialType(provider)
                        .role(Role.ROLE_USER)
                        .build()
        );

        return MemberLoginResDto.from(member);
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new NotFoundGithubEmailException();
        }

        if (memberRepository.existsByEmail(email)) {
            throw new ExistsMemberEmailException();
        }
    }

    private String getUserPicture(String picture) {
        return (picture != null) ? picture : DEFAULT_IMAGE.imageUrl;
    }

}
