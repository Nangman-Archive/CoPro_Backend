package com.example.copro.comment.domain.repository;

import com.example.copro.comment.domain.Comment;
import com.example.copro.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>, CommentCustomRepository{
    Page<Comment> findByWriter(Member member, Pageable pageable);
}
