package com.claimcheck.backend.controller;

import com.claimcheck.backend.dto.ClaimAnalysisResponse;
import com.claimcheck.backend.dto.ClaimRequest;
import com.claimcheck.backend.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.claimcheck.backend.dto.VerificationHistoryResponse;

import com.claimcheck.backend.entity.Claim;
import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ClaimAnalysisResponse> analyzeClaim(
            @Valid @RequestBody ClaimRequest request) {

        ClaimAnalysisResponse response = claimService.analyzeClaim(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{claimId}/analyze")
    public ResponseEntity<ClaimAnalysisResponse> analyzeExistingClaim(
            @PathVariable Long claimId) {

        ClaimAnalysisResponse response =
            claimService.analyzeExistingClaim(claimId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Claim>> getClaimHistory() {

        List<Claim> claims = claimService.getClaimHistory();

        return ResponseEntity.ok(claims);
    }

    @GetMapping("/{claimId}/history")
    public ResponseEntity<List<VerificationHistoryResponse>> getVerificationHistory(
            @PathVariable Long claimId) {

        List<VerificationHistoryResponse> history =
                claimService.getVerificationHistory(claimId);

        return ResponseEntity.ok(history);
    }
}