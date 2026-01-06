package com.example.demo.controller;

import com.example.demo.entities.WindowData;
import com.example.demo.entities.enums.WindowType;
import com.example.demo.repositories.ApiKeyRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.WindowDataRepository;
import com.example.demo.services.WindowRouterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/window")
public class WindowController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private WindowRouterService windowRouterService;

    @Autowired
    private WindowDataRepository windowDataRepository;

    @PostMapping("/input")
    public ResponseEntity<?> sendInput(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails user
    ) {

        if (user == null) {
            return ResponseEntity.status(401)
                    .body("JWT missing or invalid");
        }

        Long userId = userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        if (!apiKeyRepository.existsByUserIdAndActiveTrue(userId)) {
            return ResponseEntity.status(403).body(
                    Map.of(
                        "window", "API_KEY_REQUIRED",
                        "message", "Please submit API key to continue"
                    )
            );
        }

        String input = body.get("input");

        WindowType window = windowRouterService.decideWindow(input);

        WindowData data = new WindowData();
        data.setUserId(userId);
        data.setWindowType(window);
        data.setData(input);

        windowDataRepository.save(data);

        List<WindowData> history =
                windowDataRepository.findByUserIdAndWindowType(userId, window);

        return ResponseEntity.ok(
                Map.of(
                        "window", window.name(),
                        "storedMessage", input,
                        "historyCount", history.size(),
                        "history", history
                )
        );
    }
}
