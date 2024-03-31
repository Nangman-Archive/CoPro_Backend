package com.example.copro.member.domain;

import com.example.copro.board.domain.Board;
import com.example.copro.member.api.dto.request.MemberGitHubUrlUpdateReqDto;
import com.example.copro.member.api.dto.request.MemberProfileUpdateReqDto;
import com.example.copro.member.exception.InvalidGitHubUrlException;
import com.example.copro.member.exception.InvalidMemberException;
import com.example.copro.member.exception.InvalidNickNameAddressException;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣]*$");
    private static final Pattern GITHUB_URL_PATTERN = Pattern.compile("https://github\\.com/[A-Za-z0-9\\-]+");
    private static final String NOT_KNOWN = "(알수없음)";
    private static final int MAX_NICKNAME_LENGTH = 8;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Schema(description = "멤버 id", example = "1")
    private Long memberId;

    @Schema(description = "최초 로그인 구분", example = "true, false")
    private boolean firstLogin;

    @Enumerated(EnumType.STRING)
    @Schema(description = "권한", example = "ROLE_USER")
    private Role role;

    @Schema(description = "이메일", example = "abcd@gmail.com")
    private String email;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "사진 url", example = "url")
    private String picture;

    @Enumerated(value = EnumType.STRING)
    @Schema(description = "소셜로그인 타입", example = "GOOGLE, GITHUB, APPLE")
    private SocialType socialType;

    @Schema(description = "닉네임", example = "웅이")
    private String nickName;

    @Schema(description = "직군", example = "Server")
    private String occupation;

    @Schema(description = "사용 언어", example = "Java")
    private String language;

    @Schema(description = "경력", example = "1")
    private int career;

    @Schema(description = "깃허브 주소", example = "https://github.com/giwoong01")
    private String gitHubUrl;

    @Schema(description = "뷰 타입", example = "0 or 1")
    private int viewType;

    @Schema(description = "FCM토큰")
    private String fcmToken;

    @Schema(description = "탈퇴 유무")
    private boolean isDelete;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberScrapBoard> memberScrapBoards = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberLike> memberLikes = new ArrayList<>();

    @Builder
    private Member(Role role, String email, String name, String picture, SocialType socialType, boolean firstLogin, int career, int viewType) {
        this.role = role;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.socialType = socialType;
        this.career = career;
        this.viewType = viewType;
        this.firstLogin = firstLogin;
    }

    public void profileUpdate(MemberProfileUpdateReqDto memberProfileUpdateReqDto) {
        validateNickname(memberProfileUpdateReqDto.nickName());

        this.nickName = memberProfileUpdateReqDto.nickName();
        this.occupation = memberProfileUpdateReqDto.occupation();
        this.language = memberProfileUpdateReqDto.language();
        this.career = memberProfileUpdateReqDto.career();
    }

    private void validateNickname(String nickname) {
        Matcher matcher = NICKNAME_PATTERN.matcher(nickname);

        if (!matcher.matches()) {
            throw new InvalidNickNameAddressException();
        }

        if (nickname.isEmpty() || nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new InvalidMemberException(String.format("닉네임은 1자 이상 %d자 이하여야 합니다.", MAX_NICKNAME_LENGTH));
        }
    }

    public void gitHubUrlUpdate(MemberGitHubUrlUpdateReqDto memberGitHubUrlUpdateReqDto) {
        validateGitHubUrl(memberGitHubUrlUpdateReqDto.gitHubUrl());

        this.gitHubUrl = memberGitHubUrlUpdateReqDto.gitHubUrl().trim();
    }

    private void validateGitHubUrl(String gitHubUrl) {
        Matcher matcher = GITHUB_URL_PATTERN.matcher(gitHubUrl);

        if (!matcher.matches()) {
            throw new InvalidGitHubUrlException();
        }
    }

    public void firstLongUpdate() {
        this.firstLogin = false;
    }

    public void viewTypeUpdate(int viewType) {
        this.viewType = viewType;
    }
  
    public void fcmTokenUpdate(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void deleteAccount() {
        this.isDelete = true;
        this.email = null;
        this.occupation = null;
        this.fcmToken = null;
        this.nickName = NOT_KNOWN;
    }

    public void addMemberLike(Member likeMember) {
        MemberLike memberLike = new MemberLike(this, likeMember);
        this.memberLikes.add(memberLike);
    }

    public void cancelMemberLike(Member likeMember) {
        MemberLike memberLike = findMemberLike(likeMember);
        this.memberLikes.remove(memberLike);
    }

    private MemberLike findMemberLike(Member likeMember) {
        return memberLikes.stream()
                .filter(memberLike -> memberLike.getLikedMember().equals(likeMember))
                .findFirst()
                .orElse(null);
    }

    public void addScrapBoard(Board board) {
        MemberScrapBoard memberScrapBoard = MemberScrapBoard.of(board, this);
        memberScrapBoards.add(memberScrapBoard);
    }

    public void cancelScrapBoard(Board board) {
        MemberScrapBoard memberScrapBoard = findScrapBoard(board);
        memberScrapBoards.remove(memberScrapBoard);
    }

    private MemberScrapBoard findScrapBoard(Board board) {
        return memberScrapBoards.stream()
                .filter(memberScrapBoard -> memberScrapBoard.getBoard().equals(board))
                .findFirst()
                .orElse(null);
    }

    public void updateImage(String picture) {
        this.picture = picture;
    }

}
