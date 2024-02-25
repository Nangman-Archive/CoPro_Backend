package com.example.copro.notification.domain.repository;

import com.example.copro.member.domain.Member;
import com.example.copro.notification.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByMember(Member member, Pageable pageable);

    void deleteByBoardId(Long boardId);
}
