package com.claimcheck.backend.controller;

import com.claimcheck.backend.dto.ClaimAnalysisResponse;
import com.claimcheck.backend.dto.ClaimRequest;
import com.claimcheck.backend.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}