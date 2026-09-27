package com.claimcheck.backend.dto;

public class ClaimHistoryResponse {

    private Long claimId;
    private String statement;

    public ClaimHistoryResponse(
            Long claimId,
            String statement) {

        this.claimId = claimId;
        this.statement = statement;
    }

    public Long getClaimId() {
        return claimId;
    }

    public String getStatement() {
        return statement;
    }
}