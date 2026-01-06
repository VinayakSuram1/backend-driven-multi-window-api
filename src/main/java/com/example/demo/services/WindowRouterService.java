package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.entities.enums.WindowType;

@Service
public class WindowRouterService {

    public WindowType decideWindow(String input) {

        if (input.toLowerCase().contains("hello")) {
            return WindowType.WELCOME;
        }
        if (input.toLowerCase().contains("data")) {
            return WindowType.CONTEXT_A;
        }
        return WindowType.CONTEXT_B;
    }
}
