package com.blogsphere.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.blogsphere.dto.AiEnhanceRequest;
import com.blogsphere.exception.AiApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${openrouter.api.key}")
    private String openRouterApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String enhanceText(AiEnhanceRequest request) {
        String apiUrl = "https://openrouter.ai/api/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openRouterApiKey);
        headers.set("HTTP-Referer", "http://localhost:3000"); // Replace with your actual site URL
        headers.set("X-Title", "BlogSphere"); // Replace with your actual site name

        // Construct the request body for the AI model
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", "google/gemma-3n-e4b-it:free");

        ArrayNode messages = objectMapper.createArrayNode();
        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", "Enhance the following text for a blog post. Return ONLY the enhanced text, without any headings, summaries, or formatting characters (like markdown). Focus on improving vocabulary, sentence structure, and overall flow without changing the core meaning:\n\n" + request.getText());
        messages.add(userMessage);
        requestBody.set("messages", messages);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            String response = restTemplate.postForObject(apiUrl, entity, String.class);
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new AiApiException("Failed to enhance text with AI: " + e.getMessage(), e);
        }
    }
}