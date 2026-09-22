package com.claimcheck.backend.controller;

import com.claimcheck.backend.dto.SourceRequest;
import com.claimcheck.backend.entity.Claim;
import com.claimcheck.backend.entity.Source;
import com.claimcheck.backend.repository.ClaimRepository;
import com.claimcheck.backend.repository.SourceRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.claimcheck.backend.service.SourceCredibilityService;

import java.util.List;

@RestController
@RequestMapping("/api/sources")
public class SourceController {

    private final SourceRepository sourceRepository;
    private final ClaimRepository claimRepository;
    private final SourceCredibilityService sourceCredibilityService;

    public SourceController(
        SourceRepository sourceRepository,
        ClaimRepository claimRepository,
        SourceCredibilityService sourceCredibilityService) {

    this.sourceRepository = sourceRepository;
    this.claimRepository = claimRepository;
    this.sourceCredibilityService = sourceCredibilityService;
}

    @PostMapping
    public ResponseEntity<?> addSource(
            @Valid @RequestBody SourceRequest request) {

        Claim claim = claimRepository.findById(request.getClaimId())
                .orElse(null);

        if (claim == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Claim not found");
        }

        double credibilityScore =
            sourceCredibilityService.calculateScore(
                request.getSourceType()
            );

        Source source = new Source(
            request.getName(),
            request.getUrl(),
            request.getSourceType(),
            credibilityScore,
            claim
    );

        Source savedSource = sourceRepository.save(source);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedSource);
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<List<Source>> getSources(
            @PathVariable Long claimId) {

        List<Source> sources =
                sourceRepository.findByClaimId(claimId);

        return ResponseEntity.ok(sources);
    }
}