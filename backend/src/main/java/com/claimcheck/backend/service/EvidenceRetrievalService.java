package com.claimcheck.backend.service;

import com.claimcheck.backend.entity.Claim;
import com.claimcheck.backend.entity.Evidence;
import com.claimcheck.backend.repository.EvidenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvidenceRetrievalService {

    private final EvidenceRepository evidenceRepository;

    public EvidenceRetrievalService(EvidenceRepository evidenceRepository) {
        this.evidenceRepository = evidenceRepository;
    }

    public List<Evidence> getEvidenceForClaim(Long claimId) {

        return evidenceRepository.findByClaimId(claimId);
    }
}