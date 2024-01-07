package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.RecipeDraft;

public interface AsyncRecipeService {

  void processRecipeDraft(RecipeDraft recipeDraft);

}
