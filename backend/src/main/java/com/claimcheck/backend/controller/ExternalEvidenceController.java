package com.claimcheck.backend.controller;

import com.claimcheck.backend.entity.Claim;
import com.claimcheck.backend.entity.Evidence;
import com.claimcheck.backend.service.ExternalEvidenceRetrievalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/evidence")
public class ExternalEvidenceController {

    private final ExternalEvidenceRetrievalService
            externalEvidenceRetrievalService;

    public ExternalEvidenceController(
            ExternalEvidenceRetrievalService externalEvidenceRetrievalService) {

        this.externalEvidenceRetrievalService =
                externalEvidenceRetrievalService;
    }

    @GetMapping("/search")
    public List<Evidence> searchWikipedia(
            @RequestParam String query) {

        Claim temporaryClaim = new Claim(query);

        return externalEvidenceRetrievalService.searchWikipedia(
                query,
                temporaryClaim
        );
    }
}