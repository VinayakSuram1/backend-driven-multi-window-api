package com.example.demo.controller;

import com.example.demo.entities.ApiKey;
import com.example.demo.repositories.ApiKeyRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.ApiKeyHasher;
import com.example.demo.services.ApiKeyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api-keys")
public class ApiKeyController {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiKeyHasher apiKeyHasher;

   
    @PostMapping("/submit")
    public ResponseEntity<?> submitApiKey(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails user
    ) {

        if (user == null) {
            return ResponseEntity.status(401).body("JWT missing or invalid");
        }

        String rawKey = body.get("apiKey");

        if (rawKey == null || rawKey.length() < 20) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }

        Long userId = userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        if (!apiKeyRepository.findByUserIdAndActiveTrue(userId).isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("API key already exists for this user");
        }

        ApiKey apiKey = new ApiKey();
        apiKey.setUserId(userId);
        apiKey.setHashedKey(apiKeyHasher.hash(rawKey));
        apiKey.setActive(true);

        apiKeyRepository.save(apiKey);

        return ResponseEntity.ok("API key stored securely");
    }

    @PostMapping("/rotate")
    public ResponseEntity<?> rotateApiKey(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails user
    ) {

        if (user == null) {
            return ResponseEntity.status(401).body("JWT missing or invalid");
        }

        String newKey = body.get("apiKey");

        if (newKey == null || newKey.length() < 20) {
            return ResponseEntity.badRequest().body("Invalid API key");
        }

        Long userId = userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

       
        apiKeyRepository.findByUserIdAndActiveTrue(userId)
                .forEach(key -> {
                    key.setActive(false);
                    apiKeyRepository.save(key);
                });

        ApiKey apiKey = new ApiKey();
        apiKey.setUserId(userId);
        apiKey.setHashedKey(apiKeyHasher.hash(newKey));
        apiKey.setActive(true);

        apiKeyRepository.save(apiKey);

        return ResponseEntity.ok("API key rotated successfully");
    }
    
}
