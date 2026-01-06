package com.example.demo.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component 
public class ApiKeyHasher {

    public String hash(String apiKey) {
        return BCrypt.hashpw(apiKey, BCrypt.gensalt(12));
    }

    public boolean matches(String raw, String hashed) {
        return BCrypt.checkpw(raw, hashed);
    }
}
