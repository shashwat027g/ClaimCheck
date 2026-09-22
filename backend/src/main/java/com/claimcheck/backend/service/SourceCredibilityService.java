package com.claimcheck.backend.service;

import org.springframework.stereotype.Service;

@Service
public class SourceCredibilityService {

    public double calculateScore(String sourceType) {

        if (sourceType == null || sourceType.isBlank()) {
            return 0.30;
        }

        return switch (sourceType.toUpperCase()) {

            case "GOVERNMENT" -> 0.95;

            case "ACADEMIC" -> 0.90;

            case "INTERNATIONAL_ORGANIZATION" -> 0.90;

            case "ESTABLISHED_NEWS" -> 0.80;

            case "ORGANIZATION" -> 0.75;

            case "BLOG" -> 0.45;

            case "SOCIAL_MEDIA" -> 0.25;

            default -> 0.50;
        };
    }
}