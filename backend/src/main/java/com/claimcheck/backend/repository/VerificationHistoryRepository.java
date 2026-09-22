package com.claimcheck.backend.repository;

import com.claimcheck.backend.entity.VerificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationHistoryRepository
        extends JpaRepository<VerificationHistory, Long> {

    List<VerificationHistory> findByClaimIdOrderByVerifiedAtDesc(Long claimId);
}