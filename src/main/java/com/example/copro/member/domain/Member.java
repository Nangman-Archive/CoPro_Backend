package com.example.copro.member.domain;

import com.example.copro.member.api.dto.request.MemberProfileUpdateReqDto;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Schema(description = "멤버 id", example = "1")
    private Long memberId;

    @Schema(description = "UID 번호", example = "firebase UID 번호")
    private String memberName;

    @Enumerated(EnumType.STRING)
    @Schema(description = "권한", example = "ROLE_USER")
    private Role role;

    @Schema(description = "이메일", example = "abcd@gmail.com")
    private String email;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "사진 url", example = "url")
    private String picture;

    @Schema(description = "직군", example = "Server")
    private String occupation;

    @Schema(description = "사용 언어", example = "Java")
    private String language;

    @Schema(description = "경력", example = "1년")
    private String career;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MemberScrapBoard> memberScrapBoard = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return memberName;
    }

    @Builder
    private Member(String memberName, Role role, String email, String name, String picture) {
        this.memberName = memberName;
        this.role = role;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public void update(FirebaseToken token) {
        this.memberName = token.getUid();
        this.email = token.getEmail();
        this.name = token.getName();
        this.picture = token.getPicture();
    }

    public void profileUpdate(MemberProfileUpdateReqDto memberProfileUpdateReqDto) {
        this.occupation = memberProfileUpdateReqDto.occupation();
        this.language = memberProfileUpdateReqDto.language();
        this.career = memberProfileUpdateReqDto.career();
    }

}
