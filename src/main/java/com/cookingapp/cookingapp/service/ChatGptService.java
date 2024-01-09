package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.RecipeDto;

public interface ChatGptService {
    String getDifficultyLevelAndTerms(RecipeDto recipe);
    String getDifficultyLevelAndCategory(RecipeDto recipe);
    String getTerms(RecipeDto recipe);
}
