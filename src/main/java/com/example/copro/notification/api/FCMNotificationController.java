package com.example.copro.notification.api;

import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.domain.Member;
import com.example.copro.notification.api.dto.request.FCMNotificationReqDto;
import com.example.copro.notification.api.dto.request.FCMTokenReqDto;
import com.example.copro.notification.application.FCMNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(summary = "FCM토큰 등록", description = "FCM토큰을 등록 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/fcm-token")
    public RspTemplate<String> getFCMToken(@AuthenticationPrincipal Member member,
                                           @Valid @RequestBody FCMTokenReqDto fcmTokenReqDto) {
        fcmNotificationService.fcmTokenUpdate(member, fcmTokenReqDto);
        return new RspTemplate<>(HttpStatus.OK, "FCM토큰을 저장합니다.");
    }

    @Operation(summary = "채팅 알림", description = "채팅 알림을 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전송 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/chatting/notification")
    public RspTemplate<String> sendNotificationByToken(@AuthenticationPrincipal Member member,
                                                       @Valid @RequestBody FCMNotificationReqDto fcmNotificationReqDto) {
        String successMessage = fcmNotificationService.sendChattingNotification(fcmNotificationReqDto);
        return new RspTemplate<>(HttpStatus.OK, "채팅 알림을 보냅니다.", successMessage);
    }

}
