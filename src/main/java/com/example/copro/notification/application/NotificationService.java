package com.example.copro.notification.application;

import com.example.copro.board.domain.Board;
import com.example.copro.member.domain.Member;
import com.example.copro.notification.api.dto.response.NotificationResDto;
import com.example.copro.notification.domain.Notification;
import com.example.copro.notification.domain.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void notificationLikeSave(Member likeMember, Member member, String message) {
        Notification notification = Notification.builder()
                .member(likeMember)
                .message(member.getNickName() + message)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void notificationBoardSave(Board board, Member member, String message) {
        Notification notification = Notification.builder()
                .member(board.getMember())
                .board(board.getBoardId())
                .message(member.getNickName() + message)
                .build();

        notificationRepository.save(notification);
    }

    public Page<NotificationResDto> notificationList(Member member, int page, int size) {
        Page<Notification> notifications = notificationRepository.findByMember(member, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "notificationId")));

        return notifications.map(this::mapToNotification);
    }

    private NotificationResDto mapToNotification(Notification notification) {
        return NotificationResDto.from(notification);
    }

}
