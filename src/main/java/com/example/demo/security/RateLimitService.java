package com.example.demo.security;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createBucket(int capacity, int minutes) {
        return Bucket.builder()
                .addLimit(
                        Bandwidth.classic(
                                capacity,
                                Refill.greedy(capacity, Duration.ofMinutes(minutes))
                        )
                )
                .build();
    }

    public boolean allowLogin(String ip) {
        Bucket bucket = cache.computeIfAbsent(
                "LOGIN_" + ip,
                k -> createBucket(5, 1)
        );
        return bucket.tryConsume(1);
    }

    public boolean allowRegister(String ip) {
        Bucket bucket = cache.computeIfAbsent(
                "REGISTER_" + ip,
                k -> createBucket(3, 1)
        );
        return bucket.tryConsume(1);
    }
}
