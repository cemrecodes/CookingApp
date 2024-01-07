package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.RecipeDraft;
import com.cookingapp.cookingapp.entity.RecipeStatus;
import java.util.List;

public interface RecipeDraftService {

  RecipeDraft save(RecipeDraft recipeDraft);

  RecipeDraft getRecipeDraftById(Long id);

  List<RecipeDraft> getRecipeDraftsByMemberId(Long memberId);

  List<RecipeDraft> getRecipeDraftsByStatus(RecipeStatus status);

}
