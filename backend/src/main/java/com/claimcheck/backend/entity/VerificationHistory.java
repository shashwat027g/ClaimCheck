package com.claimcheck.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_history")
public class VerificationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(nullable = false)
    private String verdict;

    @Column(nullable = false)
    private Double confidence;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(nullable = false)
    private LocalDateTime verifiedAt;

    public VerificationHistory() {
    }

    public VerificationHistory(
            Claim claim,
            String verdict,
            Double confidence,
            String explanation,
            LocalDateTime verifiedAt) {

        this.claim = claim;
        this.verdict = verdict;
        this.confidence = confidence;
        this.explanation = explanation;
        this.verifiedAt = verifiedAt;
    }

    public Long getId() {
        return id;
    }

    public Claim getClaim() {
        return claim;
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