package com.claimcheck.backend.repository;

import com.claimcheck.backend.entity.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {

    List<Evidence> findByClaimId(Long claimId);
}