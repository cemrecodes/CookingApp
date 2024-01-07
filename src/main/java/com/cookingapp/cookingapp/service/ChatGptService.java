package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.entity.RecipeDraft;

public interface ChatGptService {
    String getDifficultyLevelAndTerms(RecipeDto recipe);
    String getDifficultyLevelAndCategory(RecipeDto recipe);
    String getTerms(RecipeDto recipe);

    String getRecipeDraftApproval(RecipeDraft recipeDraft);
}
