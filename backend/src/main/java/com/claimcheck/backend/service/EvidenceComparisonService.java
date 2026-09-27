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

        String claimText = claim.toLowerCase().trim();

        boolean supported = false;
        boolean contradicted = false;

        for (Evidence evidence : evidenceList) {

            String evidenceText =
                    evidence.getContent().toLowerCase();

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

        if (contradicted) {
            return "CONTRADICTED";
        }

        if (supported) {
            return "SUPPORTED";
        }

        return "INSUFFICIENT_EVIDENCE";
    }

    private boolean supportsClaim(
            String claim,
            String evidence) {

        /*
         * Earth is round
         */
        if (claim.contains("earth")
                && (claim.contains("round")
                || claim.contains("spherical"))) {

            return evidence.contains("earth")
                    && (evidence.contains("spherical earth")
                    || evidence.contains("spherical shape")
                    || evidence.contains("spherical")
                    || evidence.contains("sphericity"));
        }

        /*
         * Earth is flat
         *
         * We do not consider the words "earth" and "flat"
         * alone as evidence.
         */
        if (claim.contains("earth")
                && claim.contains("flat")) {

            return evidence.contains("earth")
                    && evidence.contains("flat")
                    && (evidence.contains("flat earth")
                    || evidence.contains("earth is flat"))
                    && !containsContradictionPhrase(evidence);
        }

        /*
         * Sun revolves around Earth
         */
        if (claim.contains("sun")
                && claim.contains("revolves")
                && claim.contains("earth")) {

            return false;
        }

        /*
         * Generic matching.
         *
         * Require at least three meaningful matching words
         * instead of only two.
         */
        String[] importantWords =
                claim.split("\\W+");

        int meaningfulWords = 0;
        int matches = 0;

        for (String word : importantWords) {

            if (word.length() < 5) {
                continue;
            }

            meaningfulWords++;

            if (evidence.contains(word)
                    || hasRelatedWord(word, evidence)) {

                matches++;
            }
        }

        /*
         * Do not treat a claim as supported when it has
         * only one or two meaningful words.
         */
        if (meaningfulWords < 3) {
            return false;
        }

        /*
         * At least 60% of meaningful claim words must
         * appear in the evidence.
         */
        return matches >= 3
                && ((double) matches / meaningfulWords) >= 0.60;
    }

    private boolean contradictsClaim(
            String claim,
            String evidence) {

        /*
         * Earth is flat
         */
        if (claim.contains("earth")
                && claim.contains("flat")) {

            if (evidence.contains("scientifically disproven")
                    || evidence.contains("disproven")
                    || evidence.contains("sphericity")
                    || evidence.contains("spherical earth")
                    || evidence.contains("spherical")
                    || evidence.contains("earth's roundness")
                    || evidence.contains("earth is round")) {

                return true;
            }
        }

        /*
         * Earth is round
         */
        if (claim.contains("earth")
                && (claim.contains("round")
                || claim.contains("spherical"))) {

            if (evidence.contains("flat earth")
                    && (evidence.contains("disproven")
                    || evidence.contains("false")
                    || evidence.contains("error"))) {

                return false;
            }
        }

        /*
         * General contradiction indicators
         */
        if (evidence.contains("false")
                || evidence.contains("incorrect")
                || evidence.contains("not true")
                || evidence.contains("no evidence")
                || evidence.contains("disproven")) {

            return true;
        }

        /*
         * Sun revolves around Earth
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

    private boolean containsContradictionPhrase(
            String evidence) {

        return evidence.contains("disproven")
                || evidence.contains("false")
                || evidence.contains("incorrect")
                || evidence.contains("not true")
                || evidence.contains("error")
                || evidence.contains("pseudoscientific");
    }

    private boolean hasRelatedWord(
            String word,
            String evidence) {

        return switch (word) {

            case "round" -> evidence.contains("spherical")
                    || evidence.contains("sphere");

            case "spherical" -> evidence.contains("round")
                    || evidence.contains("sphere");

            case "large" -> evidence.contains("big")
                    || evidence.contains("huge");

            case "small" -> evidence.contains("little")
                    || evidence.contains("tiny");

            case "doctor" -> evidence.contains("physician");

            case "physician" -> evidence.contains("doctor");

            case "purchase" -> evidence.contains("buy")
                    || evidence.contains("bought")
                    || evidence.contains("purchased");

            case "purchased" -> evidence.contains("buy")
                    || evidence.contains("bought")
                    || evidence.contains("purchase");

            default -> false;
        };
    }
}