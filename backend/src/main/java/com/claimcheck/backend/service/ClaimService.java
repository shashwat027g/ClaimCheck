package com.claimcheck.backend.service;

import com.claimcheck.backend.dto.ClaimAnalysisResponse;
import com.claimcheck.backend.dto.ClaimRequest;
import com.claimcheck.backend.dto.DecomposedClaim;
import com.claimcheck.backend.entity.Claim;
import com.claimcheck.backend.entity.ClaimType;
import com.claimcheck.backend.entity.Evidence;
import com.claimcheck.backend.repository.ClaimRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimClassifierService claimClassifierService;
    private final ClaimDecompositionService claimDecompositionService;
    private final EvidenceRetrievalService evidenceRetrievalService;

    public ClaimService(
            ClaimRepository claimRepository,
            ClaimClassifierService claimClassifierService,
            ClaimDecompositionService claimDecompositionService,
            EvidenceRetrievalService evidenceRetrievalService) {

        this.claimRepository = claimRepository;
        this.claimClassifierService = claimClassifierService;
        this.claimDecompositionService = claimDecompositionService;
        this.evidenceRetrievalService = evidenceRetrievalService;
    }

    public ClaimAnalysisResponse analyzeClaim(ClaimRequest request) {

        // Save the original claim
        Claim claim = new Claim(request.getStatement());
        Claim savedClaim = claimRepository.save(claim);

        // Classify the claim
        ClaimType claimType =
                claimClassifierService.classify(savedClaim.getStatement());

        // Decompose the claim
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

        // Retrieve evidence connected to this claim
        List<Evidence> evidenceList =
                evidenceRetrievalService.getEvidenceForClaim(savedClaim.getId());

        // Temporary verdict until evidence comparison is implemented
        String verdict = "Insufficient evidence";
        double confidence = 0.0;

        String explanation;

        if (evidenceList.isEmpty()) {

            explanation =
                    "The claim has been classified and decomposed, "
                    + "but no evidence has been retrieved yet.";

        } else {

            explanation =
                    evidenceList.size()
                    + " evidence item(s) were retrieved. "
                    + "Evidence comparison has not been implemented yet.";
        }

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