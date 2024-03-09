package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.dto.ChatGptRequest;
import com.cookingapp.cookingapp.dto.ChatGptResponse;
import com.cookingapp.cookingapp.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/v1/ai-test")
@RequiredArgsConstructor
public class AIController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final GeminiService geminiService;
    private final RestTemplate restTemplate;

    @PostMapping("/gpt")
    public String getPromptFromChatGPT(@RequestBody String prompt) {
        ChatGptRequest request = new ChatGptRequest(model, prompt);
        ChatGptResponse chatGptResponse = restTemplate.postForObject(apiUrl, request, ChatGptResponse.class);
      assert chatGptResponse != null;
      return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

    @PostMapping("/gemini")
    public String getPromptFromGemini(@RequestBody String prompt) {
        return geminiService.getCompletion(prompt);
        //return geminiService.chatDiscussion(prompt);
    }

}
