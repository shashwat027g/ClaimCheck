package com.claimcheck.backend.dto;

public class ClaimAnalysisResponse {

    private Long claimId;
    private String statement;
    private String claimType;
    private String verdict;
    private double confidence;
    private String explanation;

    public ClaimAnalysisResponse(
            Long claimId,
            String statement,
            String claimType,
            String verdict,
            double confidence,
            String explanation) {

        this.claimId = claimId;
        this.statement = statement;
        this.claimType = claimType;
        this.verdict = verdict;
        this.confidence = confidence;
        this.explanation = explanation;
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

    public String getVerdict() {
        return verdict;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getExplanation() {
        return explanation;
    }
}