package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.util.JsonStructure.GeminiRequest;
import com.cookingapp.cookingapp.util.JsonStructure.GeminiResponse;
import com.cookingapp.cookingapp.util.JsonStructure.ModelList;


public interface GeminiService {

  ModelList getModels();

  GeminiResponse getCompletion(GeminiRequest request);

  GeminiResponse getCompletionWithModel(String model, GeminiRequest request);

  GeminiResponse getCompletionWithImage(GeminiRequest request);

  String getCompletion(String text);

  String getCompletionWithImage(String text, String imageFileName);

  String getDifficultyLevelAndCategory(RecipeDto recipe);

}
