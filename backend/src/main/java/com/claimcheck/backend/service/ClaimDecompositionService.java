package com.claimcheck.backend.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClaimDecompositionService {

    public List<String> decompose(String statement) {

        List<String> claims = new ArrayList<>();

        String[] parts = statement.split(
                "\\s+(?:and|but|while|whereas)\\s+|[;]"
        );

        for (String part : parts) {

            String claim = part.trim();

            if (!claim.isEmpty()) {
                claims.add(claim);
            }
        }

        return claims;
    }
}