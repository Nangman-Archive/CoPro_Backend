package com.example.copro.auth.application;

import static com.example.copro.image.api.dto.response.DefaultImage.DEFAULT_IMAGE;

import com.example.copro.auth.api.dto.response.MemberLoginResDto;
import com.example.copro.auth.api.dto.response.UserInfo;
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
        if (!memberRepository.existsByEmail(userInfo.email())) {
            String userPicture = (userInfo.picture() != null) ? userInfo.picture() : DEFAULT_IMAGE.imageUrl;

            memberRepository.save(
                    Member.builder()
                            .email(userInfo.email())
                            .name(userInfo.name())
                            .picture(userPicture)
                            .socialType(provider)
                            .role(Role.ROLE_USER)
                            .build()
            );
        }

        Member member = memberRepository.findByEmail(userInfo.email()).orElseThrow();

        return MemberLoginResDto.from(member);
    }

}
