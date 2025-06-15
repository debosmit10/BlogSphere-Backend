package com.blogsphere.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogsphere.dto.AiEnhanceRequest;
import com.blogsphere.service.AiService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/enhance-text")
    public ResponseEntity<String> enhanceText(@RequestBody AiEnhanceRequest request) {
        String enhancedText = aiService.enhanceText(request);
        return ResponseEntity.ok(enhancedText);
    }
}