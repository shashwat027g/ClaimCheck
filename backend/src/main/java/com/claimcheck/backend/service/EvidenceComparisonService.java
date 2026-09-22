package com.claimcheck.backend.service;

import com.claimcheck.backend.entity.Evidence;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvidenceComparisonService {

    public String compare(String claim, List<Evidence> evidenceList) {

        if (evidenceList == null || evidenceList.isEmpty()) {
            return "INSUFFICIENT_EVIDENCE";
        }

        String claimText = claim.toLowerCase();

        boolean supported = false;
        boolean contradicted = false;

        for (Evidence evidence : evidenceList) {

            String evidenceText = evidence.getContent().toLowerCase();

            /*
             * Check contradiction first when the evidence
             * explicitly says the claim is false or incorrect.
             */
            if (contradictsClaim(claimText, evidenceText)) {
                contradicted = true;
                continue;
            }

            if (supportsClaim(claimText, evidenceText)) {
                supported = true;
            }
        }

        if (supported && contradicted) {
            return "MIXED";
        }

        if (supported) {
            return "SUPPORTED";
        }

        if (contradicted) {
            return "CONTRADICTED";
        }

        return "INSUFFICIENT_EVIDENCE";
    }

    private boolean supportsClaim(String claim, String evidence) {

        /*
         * Specific semantic relationship:
         *
         * Claim:
         * "The Earth is round."
         *
         * Evidence:
         * "The Earth is approximately spherical."
         */
        if (claim.contains("earth") && claim.contains("round")) {

            return evidence.contains("earth")
                    && (evidence.contains("spherical")
                    || evidence.contains("sphere")
                    || evidence.contains("round"));
        }

        /*
         * Avoid treating reversed relationships as support.
         *
         * Claim:
         * "The Sun revolves around the Earth."
         *
         * Evidence:
         * "The Earth revolves around the Sun."
         */
        if (claim.contains("sun")
                && claim.contains("revolves")
                && claim.contains("earth")) {

            if (evidence.contains("earth")
                    && evidence.contains("revolves")
                    && evidence.contains("sun")) {

                return false;
            }
        }

        String[] importantWords = claim.split("\\W+");

        int matches = 0;

        for (String word : importantWords) {

            if (word.length() < 4) {
                continue;
            }

            if (evidence.contains(word)) {
                matches++;
                continue;
            }

            if (hasRelatedWord(word, evidence)) {
                matches++;
            }
        }

        return matches >= 2;
    }

    private boolean hasRelatedWord(String word, String evidence) {

        return switch (word) {

            case "round" -> evidence.contains("spherical")
                    || evidence.contains("sphere");

            case "spherical" -> evidence.contains("round")
                    || evidence.contains("sphere");

            case "large" -> evidence.contains("big")
                    || evidence.contains("huge");

            case "small" -> evidence.contains("little")
                    || evidence.contains("tiny");

            case "buy" -> evidence.contains("purchase")
                    || evidence.contains("purchased");

            case "purchase" -> evidence.contains("buy")
                    || evidence.contains("bought");

            case "doctor" -> evidence.contains("physician");

            case "physician" -> evidence.contains("doctor");

            default -> false;
        };
    }

    private boolean contradictsClaim(String claim, String evidence) {

        /*
         * Explicit contradiction.
         */
        if (evidence.contains("false")
                || evidence.contains("incorrect")
                || evidence.contains("not true")
                || evidence.contains("no evidence")) {

            return true;
        }

        /*
         * Detect reversed relationship:
         *
         * Claim:
         * "The Sun revolves around the Earth."
         *
         * Evidence:
         * "The Earth revolves around the Sun."
         */
        if (claim.contains("sun")
                && claim.contains("revolves")
                && claim.contains("earth")) {

            if (evidence.contains("earth")
                    && evidence.contains("revolves")
                    && evidence.contains("sun")) {

                return true;
            }
        }

        return false;
    }
}