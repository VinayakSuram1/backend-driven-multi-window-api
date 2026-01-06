package com.example.demo.services;

import org.springframework.stereotype.Service;

@Service
public class ApiKeyService {

    public void validateApiKey(String apiKey) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key is required");
        }

        if (apiKey.length() < 20) {
            throw new IllegalArgumentException("API key must be at least 20 characters");
        }

        if (apiKey.contains(" ")) {
            throw new IllegalArgumentException("API key must not contain spaces");
        }
    }
}
