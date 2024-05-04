package com.example.copro.member.domain.repository;

import com.example.copro.member.domain.BlockedMemberMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedMemberMappingRepository extends JpaRepository<BlockedMemberMapping, Long> {
}
