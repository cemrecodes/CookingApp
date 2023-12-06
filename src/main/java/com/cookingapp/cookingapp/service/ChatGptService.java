package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.ChatGptRequest;
import com.cookingapp.cookingapp.dto.ChatGptResponse;
import com.cookingapp.cookingapp.dto.FoodRecipeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGptService {


    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate template;

    private final String promptBase = "Aşağıdaki yemek tarifinin zorluğunu belirle(kolay,orta,zor)." +
            "Tarifte geçen bilinmedik yemek yapma terimlerini varsa tespit et ve kısa bir açıklama ekle, " +
            "tarifte özel terim bulunmayabileceğini göz önünde bulundur." +
            "(Örneğin: benmari usulü, marine etmek, julienne doğrama vb.)\n" +
            "Cevabı dictionary şeklinde ver, zorluk derecesi key'i \"difficulty\", özel terimler key'i ise \"terms\" içerisinde key 0'an başlayarak sayı," +
            " value \"terim\", \"açıklama\" şeklinde liste olsun. Örnek: {\"terms:\":[\"0\":\"terim\", \"açıklaması\"]} " +
            "Tarif: ``` ";

    public String getDifficultyLevelAndTerms(FoodRecipeDto recipe) {
        StringBuilder prompt = new StringBuilder(promptBase);
        prompt.append(recipe.toStringForChatGpt());
        prompt.append(" ```");
        ChatGptRequest request = new ChatGptRequest(model, prompt.toString());
        ChatGptResponse chatGptResponse = template.postForObject(apiUrl, request, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }
}
