package com.claimcheck.backend.controller;

import com.claimcheck.backend.dto.EvidenceRequest;
import com.claimcheck.backend.entity.Claim;
import com.claimcheck.backend.entity.Evidence;
import com.claimcheck.backend.repository.ClaimRepository;
import com.claimcheck.backend.repository.EvidenceRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evidence")
public class EvidenceController {

    private final EvidenceRepository evidenceRepository;
    private final ClaimRepository claimRepository;

    public EvidenceController(
            EvidenceRepository evidenceRepository,
            ClaimRepository claimRepository) {

        this.evidenceRepository = evidenceRepository;
        this.claimRepository = claimRepository;
    }

    @PostMapping
    public ResponseEntity<?> addEvidence(
            @Valid @RequestBody EvidenceRequest request) {

        Claim claim = claimRepository.findById(request.getClaimId())
                .orElse(null);

        if (claim == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Claim not found");
        }

        Evidence evidence = new Evidence(
                request.getContent(),
                request.getUrl(),
                request.getTitle(),
                request.getSourceName(),
                claim
        );

        Evidence savedEvidence = evidenceRepository.save(evidence);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedEvidence);
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<List<Evidence>> getEvidence(
            @PathVariable Long claimId) {

        List<Evidence> evidence =
                evidenceRepository.findByClaimId(claimId);

        return ResponseEntity.ok(evidence);
    }
}