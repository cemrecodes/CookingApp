package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.util.JsonStructure.Content;
import com.cookingapp.cookingapp.util.JsonStructure.GeminiRequest;
import com.cookingapp.cookingapp.util.JsonStructure.GeminiResponse;
import com.cookingapp.cookingapp.util.JsonStructure.InlineData;
import com.cookingapp.cookingapp.util.JsonStructure.InlineDataPart;
import com.cookingapp.cookingapp.util.JsonStructure.ModelList;
import com.cookingapp.cookingapp.util.JsonStructure.TextPart;
import java.io.InputStream;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiService {
  public static final String GEMINI_PRO = "gemini-pro";
  public static final String GEMINI_ULTIMATE = "gemini-ultimate";
  public static final String GEMINI_PRO_VISION = "gemini-pro-vision";

  private final GeminiInterface geminiInterface;

  public ModelList getModels() {
    return geminiInterface.getModels();
  }

  public GeminiResponse getCompletion(GeminiRequest request) {
    return geminiInterface.getCompletion(GEMINI_PRO, request);
  }

  public GeminiResponse getCompletionWithModel(String model, GeminiRequest request) {
    return geminiInterface.getCompletion(model, request);
  }


  public GeminiResponse getCompletionWithImage(GeminiRequest request) {
    return geminiInterface.getCompletion(GEMINI_PRO_VISION, request);
  }

  public String getCompletion(String text) {
    GeminiResponse response = getCompletion(new GeminiRequest(
        List.of(new Content(List.of(new TextPart(text))))));
    return response.candidates().get(0).content().parts().get(0).text();
  }

  public String getCompletionWithImage(String text, String imageUrl) {
    GeminiResponse response = null;
    try {
      InputStream in = new URL(imageUrl).openStream();
      byte[] imageBytes = in.readAllBytes();
      String base64Image = Base64.getEncoder().encodeToString(imageBytes);

      response = getCompletionWithImage(
          new GeminiRequest(List.of(new Content(List.of(
              new TextPart(text),
              new InlineDataPart(new InlineData("image/png", base64Image))
          ))))
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return response.candidates().get(0).content().parts().get(0).text();
  }

  public String getDifficultyLevelAndCategory(RecipeDto recipe) {
    String message = "Aşağıdaki yemek tarifinin zorluk derecesini (kolay,orta,zor) ve kategorisini (çorba, ana yemek, tatlı, içecek) belirle." +
        "Cevabı direkt json biçiminde yolla, zorluk derecesi key'i \"difficulty\", kategori keyi ise \"category\" olsun. Cevabın başına json vs yazı EKLEME!";

    String sb = message + recipe.toStringForAI();
    GeminiResponse response = getCompletion(new GeminiRequest(
        List.of(new Content(List.of(new TextPart(sb))))));
    return response.candidates().get(0).content().parts().get(0).text();
  }

  public String explainInstruction(String instruction) {
    String message = "Aşağıda bir yemek tarifinin yapılışından bir madde bulunuyor. Bu maddeyi açıkla fakat "
        + "verilen maddenin dışında bir açıklama yapma, sadece verilen cümleyi açıkla. Açıklama plain text ve paragraf şeklinde olsun."
        + " Madde: ";

    String sb = message + instruction;
    GeminiResponse response = getCompletion(new GeminiRequest(
        List.of(new Content(List.of(new TextPart(sb))))));
    return response.candidates().get(0).content().parts().get(0).text();
  }
}

/*
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeminiService {

    String projectId = "cooking-416414";
    String location = "europe-west4";
    String modelName = "gemini-1.0-pro";

    String difficultyAndCategoryQuestion = "Aşağıdaki yemek tarifinin zorluğunu (kolay,orta,zor) ve kategorisini (çorba, ana yemek, tatlı, içecek) belirle." +
            "Cevabı dictionary şeklinde ver, zorluk derecesi key'i \"difficulty\", kategori keyi ise \"category\" olsun.";
    public String chatDiscussion(String difficultyAndCategoryQuestion)
        throws IOException {
      // Initialize client that will be used to send requests. This client only needs
      // to be created once, and can be reused for multiple requests.
      try (VertexAI vertexAI = new VertexAI(projectId, location)) {
        GenerateContentResponse response;

        GenerativeModel model = new GenerativeModel(modelName, vertexAI);
        // Create a chat session to be used for interactive conversation.
        ChatSession chatSession = new ChatSession(model);

        response = chatSession.sendMessage(difficultyAndCategoryQuestion);
        log.info(ResponseHandler.getText(response));

        return String.valueOf(response);
    }
  }
}

*/