package com.claimcheck.backend.service;

import com.claimcheck.backend.dto.VerificationHistoryResponse;
import com.claimcheck.backend.dto.ClaimAnalysisResponse;
import com.claimcheck.backend.dto.ClaimRequest;
import com.claimcheck.backend.dto.DecomposedClaim;
import com.claimcheck.backend.entity.Claim;
import com.claimcheck.backend.entity.ClaimType;
import com.claimcheck.backend.entity.Evidence;
import com.claimcheck.backend.repository.ClaimRepository;
import org.springframework.stereotype.Service;
import com.claimcheck.backend.entity.VerificationHistory;
import com.claimcheck.backend.repository.VerificationHistoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimClassifierService claimClassifierService;
    private final VerificationHistoryRepository verificationHistoryRepository;
    private final ClaimDecompositionService claimDecompositionService;
    private final EvidenceRetrievalService evidenceRetrievalService;
    private final EvidenceComparisonService evidenceComparisonService;

    public ClaimService(
        ClaimRepository claimRepository,
        ClaimClassifierService claimClassifierService,
        ClaimDecompositionService claimDecompositionService,
        EvidenceRetrievalService evidenceRetrievalService,
        EvidenceComparisonService evidenceComparisonService,
        VerificationHistoryRepository verificationHistoryRepository) {

        this.claimRepository = claimRepository;
        this.claimClassifierService = claimClassifierService;
        this.claimDecompositionService = claimDecompositionService;
        this.evidenceRetrievalService = evidenceRetrievalService;
        this.evidenceComparisonService = evidenceComparisonService;
        this.verificationHistoryRepository = verificationHistoryRepository;
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
                evidenceRetrievalService.getEvidenceForClaim(
                        savedClaim.getId()
                );

        // Compare claim with retrieved evidence
        String verdict =
                evidenceComparisonService.compare(
                        savedClaim.getStatement(),
                        evidenceList
                );

        // Temporary confidence calculation
        double confidence = calculateConfidence(evidenceList, verdict);

        // Generate explanation
        String explanation =
                generateExplanation(evidenceList, verdict);

        VerificationHistory history = new VerificationHistory(
                claim,
                verdict,
                confidence,
                explanation,
                LocalDateTime.now()
        );

        verificationHistoryRepository.save(history);

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

    private double calculateConfidence(
            List<Evidence> evidenceList,
            String verdict) {

        if (evidenceList.isEmpty()) {
            return 0.0;
        }

        if ("SUPPORTED".equals(verdict)) {
            return 0.80;
        }

        if ("CONTRADICTED".equals(verdict)) {
            return 0.80;
        }

        if ("MIXED".equals(verdict)) {
            return 0.60;
        }

        return 0.30;
    }

    private String generateExplanation(
            List<Evidence> evidenceList,
            String verdict) {

        if (evidenceList.isEmpty()) {
            return "No evidence is available for comparison.";
        }

        return switch (verdict) {

            case "SUPPORTED" ->
                    "The available evidence contains information that supports the claim.";

            case "CONTRADICTED" ->
                    "The available evidence contains information that contradicts the claim.";

            case "MIXED" ->
                    "The available evidence contains both supporting and contradicting information.";

            default ->
                    "The available evidence does not provide enough information to verify the claim.";
        };
    }

    public ClaimAnalysisResponse analyzeExistingClaim(Long claimId) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Claim not found"));

        ClaimType claimType =
                claimClassifierService.classify(claim.getStatement());

        List<String> claimParts =
                claimDecompositionService.decompose(claim.getStatement());

        List<DecomposedClaim> decomposedClaims = new ArrayList<>();

        for (int i = 0; i < claimParts.size(); i++) {

            decomposedClaims.add(
                    new DecomposedClaim(
                           i + 1,
                           claimParts.get(i)
                   )
           );
        }
        
        List<Evidence> evidenceList =
                evidenceRetrievalService.getEvidenceForClaim(
                        claim.getId()
                );

        String verdict =
                evidenceComparisonService.compare(
                        claim.getStatement(),
                        evidenceList
                );

        double confidence =
                calculateConfidence(evidenceList, verdict);

        String explanation =
                generateExplanation(evidenceList, verdict);

        VerificationHistory history = new VerificationHistory(
                claim,
                verdict,
                confidence,
                explanation,
                LocalDateTime.now()
        );

        verificationHistoryRepository.save(history);

        return new ClaimAnalysisResponse(
                claim.getId(),
                claim.getStatement(),
                claimType.name(),
                decomposedClaims,
                verdict,
                confidence,
                explanation
        );  
    }

    public List<Claim> getClaimHistory() {

        return claimRepository.findAll(
            org.springframework.data.domain.Sort
                    .by(org.springframework.data.domain.Sort.Direction.DESC, "id")
        );
    }

    public List<VerificationHistoryResponse> getVerificationHistory(Long claimId) {

        List<VerificationHistory> history =
                verificationHistoryRepository
                        .findByClaimIdOrderByVerifiedAtDesc(claimId);

        return history.stream()
                .map(record -> new VerificationHistoryResponse(
                        record.getId(),
                        record.getClaim().getId(),
                        record.getClaim().getStatement(),
                        record.getVerdict(),
                        record.getConfidence(),
                        record.getExplanation(),
                        record.getVerifiedAt()
                ))
                .toList();
    }
}