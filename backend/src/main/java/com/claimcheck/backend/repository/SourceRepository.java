package com.claimcheck.backend.repository;

import com.claimcheck.backend.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SourceRepository extends JpaRepository<Source, Long> {

    List<Source> findByClaimId(Long claimId);
}