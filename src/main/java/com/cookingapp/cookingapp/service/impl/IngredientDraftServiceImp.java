package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.IngredientDraft;
import com.cookingapp.cookingapp.repo.IngredientDraftRepository;
import com.cookingapp.cookingapp.service.IngredientDraftService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngredientDraftServiceImp implements IngredientDraftService {

  private final IngredientDraftRepository draftRepository;

  @Override
  public IngredientDraft save(IngredientDraft ingredient) {
    return draftRepository.save(ingredient);
  }

  @Override
  public List<IngredientDraft> getByRecipeId(Long recipeId) {
    return draftRepository.getByRecipeId(recipeId);
  }

  @Override
  public void saveAll(List<IngredientDraft> ingredientDraftList) {
    draftRepository.saveAll(ingredientDraftList);
  }
}
