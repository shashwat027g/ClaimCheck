package com.claimcheck.backend.repository;

import com.claimcheck.backend.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
}