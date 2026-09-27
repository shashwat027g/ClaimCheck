package com.claimcheck.backend.dto;

public class SourceCredibilityResponse {

    private String sourceName;
    private String sourceType;
    private double credibilityScore;

    public SourceCredibilityResponse(
            String sourceName,
            String sourceType,
            double credibilityScore) {

        this.sourceName = sourceName;
        this.sourceType = sourceType;
        this.credibilityScore = credibilityScore;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public double getCredibilityScore() {
        return credibilityScore;
    }
}