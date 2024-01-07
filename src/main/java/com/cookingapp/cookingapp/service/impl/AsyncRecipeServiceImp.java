package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.IngredientDraft;
import com.cookingapp.cookingapp.entity.RecipeDraft;
import com.cookingapp.cookingapp.entity.RecipeStatus;
import com.cookingapp.cookingapp.service.AsyncRecipeService;
import com.cookingapp.cookingapp.service.ChatGptService;
import com.cookingapp.cookingapp.service.RecipeServiceFacade;
import com.cookingapp.cookingapp.util.Util;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncRecipeServiceImp implements AsyncRecipeService {

  private final RecipeServiceFacade recipeServiceFacade;

  private final ChatGptService chatGptService;

  @Override
  @Async
  public void processRecipeDraft(RecipeDraft recipeDraft) {
    String chatGptResponse = this.chatGptService.getRecipeDraftApproval(recipeDraft);
    Map<String, Object> mappedChatGptResponse = Util.convertJsonToMap(chatGptResponse);
    recipeDraft.setReason((String) mappedChatGptResponse.get("reason"));
    if((Boolean) mappedChatGptResponse.get("approved")){
      recipeDraft.setStatus(RecipeStatus.APPROVED);
      List<Ingredient> ingredientList = recipeDraft.getIngredients().stream()
          .map(IngredientDraft::toIngredient)
          .toList();
      recipeServiceFacade.saveRecipe(recipeDraft.convertToRecipe(), ingredientList, recipeDraft.getMemberId());
    }
    else {
      recipeDraft.setStatus(RecipeStatus.REJECTED);
    }
  }

}
