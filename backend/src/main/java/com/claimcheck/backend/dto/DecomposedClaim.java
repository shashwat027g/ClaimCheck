package com.claimcheck.backend.dto;

public class DecomposedClaim {

    private int claimNumber;
    private String statement;

    public DecomposedClaim(int claimNumber, String statement) {
        this.claimNumber = claimNumber;
        this.statement = statement;
    }

    public int getClaimNumber() {
        return claimNumber;
    }

    public String getStatement() {
        return statement;
    }
}