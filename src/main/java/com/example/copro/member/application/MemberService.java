package com.example.copro.member.application;

import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.Role;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.exception.MemberNotFoundException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member create(FirebaseToken firebaseToken, Role role) {
        Member member = Member.builder()
                .memberName(firebaseToken.getUid())
                .email(firebaseToken.getEmail())
                .name(firebaseToken.getName())
                .picture(firebaseToken.getPicture())
                .role(role)
                .build();

        return memberRepository.save(member);
    }

    @Transactional
    public Member updateByUsername(FirebaseToken firebaseToken) {
        Member member = memberRepository.findByMemberName(firebaseToken.getUid())
                .orElseThrow(() -> new MemberNotFoundException(String.format("해당 유저(%s)를 찾을 수 없습니다.", firebaseToken.getName())));

        member.update(firebaseToken);

        return memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Member loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByMemberName(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("해당 유저(%s)를 찾을 수 없습니다.", username)));
    }
}
