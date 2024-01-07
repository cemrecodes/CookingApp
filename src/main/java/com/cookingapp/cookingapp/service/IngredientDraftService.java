package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.IngredientDraft;
import java.util.List;

public interface IngredientDraftService {
  IngredientDraft save(IngredientDraft ingredient);

  List<IngredientDraft> getByRecipeId(Long recipeId);

  void saveAll(List<IngredientDraft> ingredientDraftList);
}
