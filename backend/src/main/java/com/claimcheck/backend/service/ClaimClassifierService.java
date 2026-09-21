package com.claimcheck.backend.service;

import com.claimcheck.backend.entity.ClaimType;
import org.springframework.stereotype.Service;

@Service
public class ClaimClassifierService {

    public ClaimType classify(String statement) {

        String text = statement.toLowerCase();

        if (containsAny(text, "population", "percent", "%", "number", "rate", "average")) {
            return ClaimType.STATISTICAL;
        }

        if (containsAny(text, "war", "independence", "historical", "ancient", "founded", "in 19", "in 20")) {
            return ClaimType.HISTORICAL;
        }

        if (containsAny(text, "temperature", "chemical", "physics", "biology",
                "gravity", "boils", "scientific", "planet")) {
            return ClaimType.SCIENTIFIC;
        }

        if (containsAny(text, "government", "president", "minister", "election",
                "policy", "political", "parliament")) {
            return ClaimType.POLITICAL;
        }

        if (containsAny(text, "revenue", "profit", "cost", "price", "crore",
                "million", "billion", "economy", "financial")) {
            return ClaimType.FINANCIAL;
        }

        if (containsAny(text, "medicine", "disease", "health", "doctor",
                "treatment", "drug", "fever", "medical")) {
            return ClaimType.MEDICAL;
        }

        if (containsAny(text, "capital", "located", "city", "country",
                "state", "river", "mountain", "geographical")) {
            return ClaimType.GEOGRAPHICAL;
        }

        if (containsAny(text, "i think", "in my opinion", "best", "worst",
                "beautiful", "better")) {
            return ClaimType.OPINION;
        }

        return ClaimType.GENERAL_FACTUAL;
    }

    private boolean containsAny(String text, String... keywords) {

        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}