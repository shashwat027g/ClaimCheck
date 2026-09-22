package com.claimcheck.backend.dto;

import java.time.LocalDateTime;

public class VerificationHistoryResponse {

    private Long id;
    private Long claimId;
    private String claimStatement;
    private String verdict;
    private Double confidence;
    private String explanation;
    private LocalDateTime verifiedAt;

    public VerificationHistoryResponse(
            Long id,
            Long claimId,
            String claimStatement,
            String verdict,
            Double confidence,
            String explanation,
            LocalDateTime verifiedAt) {

        this.id = id;
        this.claimId = claimId;
        this.claimStatement = claimStatement;
        this.verdict = verdict;
        this.confidence = confidence;
        this.explanation = explanation;
        this.verifiedAt = verifiedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getClaimId() {
        return claimId;
    }

    public String getClaimStatement() {
        return claimStatement;
    }

    public String getVerdict() {
        return verdict;
    }

    public Double getConfidence() {
        return confidence;
    }

    public String getExplanation() {
        return explanation;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }
}