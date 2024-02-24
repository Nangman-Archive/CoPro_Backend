package com.example.copro.notification.application;

import com.example.copro.board.domain.Board;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import com.example.copro.member.exception.MemberNotFoundException;
import com.example.copro.notification.api.dto.request.FCMNotificationReqDto;
import com.example.copro.notification.api.dto.request.FCMTokenReqDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public FCMNotificationService(FirebaseMessaging firebaseMessaging, MemberRepository memberRepository, NotificationService notificationService) {
        this.firebaseMessaging = firebaseMessaging;
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void fcmTokenUpdate(Member member, FCMTokenReqDto fcmTokenReqDto) {
        Member getMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(MemberNotFoundException::new);
        getMember.fcmTokenUpdate(fcmTokenReqDto.fcmToken());
    }

    public void sendLikeMemberNotification(Member member, Member likeMember) {
        String message = "님이 당신의 프로필을 좋아합니다.";
        FCMNotificationReqDto notificationReqDto = createNotification(likeMember.getEmail(), member.getNickName(), message);

        notificationService.notificationLikeSave(likeMember, member, message);

        sendNotification(notificationReqDto);
    }

    public void sendHeartBoardNotification(Board board, Member member) {
        if (!board.getMember().getMemberId().equals(member.getMemberId())) {
            String message = "님이 회원님의 게시물을 좋아합니다.";
            FCMNotificationReqDto notificationReqDto = createNotification(board.getMember().getEmail(), member.getNickName(), message);

            notificationService.notificationBoardSave(board, member, message);

            sendNotification(notificationReqDto);
        }
    }

    public void sendCommentNotification(Board board, Member member) {
        if (!board.getMember().getMemberId().equals(member.getMemberId())) {
            String message = "님이 회원님의 게시물에 댓글을 남겼습니다.";
            FCMNotificationReqDto notificationReqDto = createNotification(board.getMember().getEmail(),
                    member.getNickName(), message);

            notificationService.notificationBoardSave(board, member, message);

            sendNotification(notificationReqDto);
        }
    }

    public String sendChattingNotification(FCMNotificationReqDto reqDto) {
        return sendNotification(reqDto);
    }

    private FCMNotificationReqDto createNotification(String targetEmail, String nickName, String actionMessage) {
        return FCMNotificationReqDto.builder()
                .targetMemberEmail(targetEmail)
                .title("CoPro")
                .body(nickName + actionMessage)
                .build();
    }

    private String sendNotification(FCMNotificationReqDto reqDto) {
        Member targetMember = memberRepository.findByEmail(reqDto.targetMemberEmail()).orElseThrow(MemberNotFoundException::new);

        if (targetMember.getFcmToken() != null) {
            Notification notification = Notification.builder()
                    .setTitle(reqDto.title())
                    .setBody(reqDto.body())
                    .build();

            Message message = Message.builder()
                    .setToken(targetMember.getFcmToken())
                    .setNotification(notification)
                    .build();

            try {
                firebaseMessaging.send(message);
                return "성공적으로 알림을 보냈습니다.";
            } catch (FirebaseMessagingException e) {
                return "알림보내기를 실패했습니다.";
            }
        }

        return "해당 유저의 FCM토큰이 존재하지 않습니다. tartgetMemberId = " + targetMember.getEmail();
    }
}
