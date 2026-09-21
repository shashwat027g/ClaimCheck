package com.claimcheck.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ClaimRequest {

    @NotBlank(message = "Claim statement cannot be empty")
    private String statement;

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}