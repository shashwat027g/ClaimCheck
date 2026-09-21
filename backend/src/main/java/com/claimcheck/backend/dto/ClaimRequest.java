package com.claimcheck.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClaimRequest {

    @NotBlank(message = "Claim statement cannot be empty")
    @Size(
            min = 5,
            max = 1000,
            message = "Claim statement must be between 5 and 1000 characters"
    )
    private String statement;

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}