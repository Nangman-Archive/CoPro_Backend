package com.example.copro.notification.api;

import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.domain.Member;
import com.example.copro.notification.api.dto.request.FCMNotificationReqDto;
import com.example.copro.notification.api.dto.request.FCMTokenReqDto;
import com.example.copro.notification.application.FCMNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FCMNotificationController {

    private final FCMNotificationService fcmNotificationService;

    public FCMNotificationController(FCMNotificationService fcmNotificationService) {
        this.fcmNotificationService = fcmNotificationService;
    }

    @PostMapping("/fcm-token")
    public RspTemplate<String> getFCMToken(@AuthenticationPrincipal Member member, @RequestBody FCMTokenReqDto fcmTokenReqDto) {
        fcmNotificationService.fcmTokenUpdate(member, fcmTokenReqDto);
        return new RspTemplate<>(HttpStatus.OK, "FCM토큰을 저장합니다.");
    }

    @PostMapping("/chatting/notification")
    public String sendNotificationByToken(@AuthenticationPrincipal Member member, @RequestBody FCMNotificationReqDto fcmNotificationReqDto) {
        return fcmNotificationService.sendChattingNotification(fcmNotificationReqDto);
    }

}
