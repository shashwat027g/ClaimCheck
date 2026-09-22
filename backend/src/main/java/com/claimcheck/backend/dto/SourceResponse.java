package com.claimcheck.backend.dto;

public class SourceResponse {

    private Long id;
    private Long claimId;
    private String name;
    private String url;
    private String sourceType;
    private Double credibilityScore;

    public SourceResponse(
            Long id,
            Long claimId,
            String name,
            String url,
            String sourceType,
            Double credibilityScore) {

        this.id = id;
        this.claimId = claimId;
        this.name = name;
        this.url = url;
        this.sourceType = sourceType;
        this.credibilityScore = credibilityScore;
    }

    public Long getId() {
        return id;
    }

    public Long getClaimId() {
        return claimId;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getSourceType() {
        return sourceType;
    }

    public Double getCredibilityScore() {
        return credibilityScore;
    }
}