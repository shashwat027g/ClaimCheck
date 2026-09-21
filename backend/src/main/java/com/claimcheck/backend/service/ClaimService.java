package com.claimcheck.backend.service;

import com.claimcheck.backend.dto.ClaimAnalysisResponse;
import com.claimcheck.backend.dto.ClaimRequest;
import com.claimcheck.backend.dto.DecomposedClaim;
import com.claimcheck.backend.entity.Claim;
import com.claimcheck.backend.entity.ClaimType;
import com.claimcheck.backend.repository.ClaimRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimClassifierService claimClassifierService;
    private final ClaimDecompositionService claimDecompositionService;

    public ClaimService(
            ClaimRepository claimRepository,
            ClaimClassifierService claimClassifierService,
            ClaimDecompositionService claimDecompositionService) {

        this.claimRepository = claimRepository;
        this.claimClassifierService = claimClassifierService;
        this.claimDecompositionService = claimDecompositionService;
    }

    public ClaimAnalysisResponse analyzeClaim(ClaimRequest request) {

        // Save the original claim
        Claim claim = new Claim(request.getStatement());
        Claim savedClaim = claimRepository.save(claim);

        // Classify the claim
        ClaimType claimType =
                claimClassifierService.classify(savedClaim.getStatement());

        // Decompose the claim into individual claims
        List<String> claimParts =
                claimDecompositionService.decompose(savedClaim.getStatement());

        List<DecomposedClaim> decomposedClaims = new ArrayList<>();

        for (int i = 0; i < claimParts.size(); i++) {

            decomposedClaims.add(
                    new DecomposedClaim(
                            i + 1,
                            claimParts.get(i)
                    )
            );
        }

        // Temporary analysis result
        String verdict = "Insufficient evidence";
        double confidence = 0.0;

        String explanation =
                "The claim has been received, classified, and decomposed, "
                + "but evidence verification has not been performed yet.";

        return new ClaimAnalysisResponse(
                savedClaim.getId(),
                savedClaim.getStatement(),
                claimType.name(),
                decomposedClaims,
                verdict,
                confidence,
                explanation
        );
    }
}