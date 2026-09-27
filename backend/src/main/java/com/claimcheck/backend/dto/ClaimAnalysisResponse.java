package com.claimcheck.backend.dto;

import java.util.List;

public class ClaimAnalysisResponse {

    private Long claimId;
    private String statement;
    private String claimType;
    private List<DecomposedClaim> decomposedClaims;
    private String verdict;
    private double confidence;
    private String explanation;
    private List<EvidenceResponse> evidence;
    private List<SourceCredibilityResponse> sources;

    public ClaimAnalysisResponse(
            Long claimId,
            String statement,
            String claimType,
            List<DecomposedClaim> decomposedClaims,
            String verdict,
            double confidence,
            String explanation,
            List<EvidenceResponse> evidence,
            List<SourceCredibilityResponse> sources) {

        this.claimId = claimId;
        this.statement = statement;
        this.claimType = claimType;
        this.decomposedClaims = decomposedClaims;
        this.verdict = verdict;
        this.confidence = confidence;
        this.explanation = explanation;
        this.evidence = evidence;
        this.sources = sources;
    }

    public Long getClaimId() {
        return claimId;
    }

    public String getStatement() {
        return statement;
    }

    public String getClaimType() {
        return claimType;
    }

    public List<DecomposedClaim> getDecomposedClaims() {
        return decomposedClaims;
    }

    public String getVerdict() {
        return verdict;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getExplanation() {
        return explanation;
    }

    public List<EvidenceResponse> getEvidence() {
        return evidence;
    }

    public List<SourceCredibilityResponse> getSources() {
        return sources;
    }
}