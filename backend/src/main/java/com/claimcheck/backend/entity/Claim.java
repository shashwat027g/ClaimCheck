package com.claimcheck.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String statement;

    public Claim() {
    }

    public Claim(String statement) {
        this.statement = statement;
    }

    public Long getId() {
        return id;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}