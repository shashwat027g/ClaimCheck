package com.claimcheck.backend.service;

import com.claimcheck.backend.dto.ClaimAnalysisResponse;
import com.claimcheck.backend.dto.ClaimRequest;
import com.claimcheck.backend.entity.Claim;
import com.claimcheck.backend.entity.ClaimType;
import com.claimcheck.backend.repository.ClaimRepository;
import org.springframework.stereotype.Service;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimClassifierService claimClassifierService;

    public ClaimService(
            ClaimRepository claimRepository,
            ClaimClassifierService claimClassifierService) {

        this.claimRepository = claimRepository;
        this.claimClassifierService = claimClassifierService;
    }

    public ClaimAnalysisResponse analyzeClaim(ClaimRequest request) {

        // Save the claim in the database
        Claim claim = new Claim(request.getStatement());
        Claim savedClaim = claimRepository.save(claim);

        // Classify the claim
        ClaimType claimType =
                claimClassifierService.classify(savedClaim.getStatement());

        // Temporary analysis result
        // Real evidence verification will be added later
        String verdict = "Insufficient evidence";
        double confidence = 0.0;
        String explanation =
                "The claim has been received and classified, " +
                "but evidence verification has not been performed yet.";

        return new ClaimAnalysisResponse(
                savedClaim.getId(),
                savedClaim.getStatement(),
                claimType.name(),
                verdict,
                confidence,
                explanation
        );
    }
}