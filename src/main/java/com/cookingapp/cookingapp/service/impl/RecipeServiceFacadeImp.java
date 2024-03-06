package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.entity.Category;
import com.cookingapp.cookingapp.entity.DifficultyLevel;
import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.service.ChatGptService;
import com.cookingapp.cookingapp.service.GeminiService;
import com.cookingapp.cookingapp.service.IngredientService;
import com.cookingapp.cookingapp.service.RecipeESService;
import com.cookingapp.cookingapp.service.RecipeMemberService;
import com.cookingapp.cookingapp.service.RecipeService;
import com.cookingapp.cookingapp.service.RecipeServiceFacade;
import com.cookingapp.cookingapp.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeServiceFacadeImp implements RecipeServiceFacade {

  private final RecipeService recipeService;

  private final IngredientService ingredientService;

  private final RecipeESService recipeESService;

  private final RecipeMemberService recipeMemberService;

  private final ChatGptService chatGptService;

  private final GeminiService geminiService;

  @Override
  public Recipe saveRecipe(Recipe recipe, List<Ingredient> ingredientList) {
    setDifficultyAndCategory(recipe);
    Recipe finalRecipe = recipeService.save(recipe);
    List<Ingredient> list = new ArrayList<>();
    for (Ingredient ingredient : ingredientList) {
      ingredient.setRecipe(finalRecipe);
      list.add(ingredient);
    }
    recipe.setIngredients( ingredientService.saveAll(list) );
    recipeESService.save(recipe.toRecipeES());
    return recipe;
  }

  @Override
  public Recipe saveRecipe(Recipe recipe, List<Ingredient> ingredientList, Member member) {
    setDifficulty(recipe);
    Recipe finalRecipe = recipeService.save(recipe);
    List<Ingredient> list = new ArrayList<>();
    for (Ingredient ingredient : ingredientList) {
      ingredient.setRecipe(finalRecipe);
      list.add(ingredient);
    }
    ingredientList = list;
    ingredientList = ingredientService.saveAll(ingredientList);
    recipe.setIngredients(ingredientList);
    recipeESService.save(recipe.toRecipeES());
    recipeMemberService.save(recipe, member);
    return recipe;
  }

  /* gets difficulty and category of recipe from chatgpt */
  private Map<String, Object> getDifficultyAndCategory(RecipeDto recipe) {
    String aiResponse = geminiService.getDifficultyLevelAndCategory(recipe);
        // this.chatGptService.getDifficultyLevelAndCategory(recipe);
    return Util.convertJsonToMap(aiResponse);
  }

  private void setDifficultyAndCategory(Recipe recipe) {
    Map<String, Object> difficultyAndCategory = this.getDifficultyAndCategory(recipe.toDto());
    recipe.setDifficultyLevel(DifficultyLevel.convert((String) difficultyAndCategory.get("difficulty")));
    recipe.setCategory(Category.convert((String) difficultyAndCategory.get("category")));
  }

  private void setDifficulty(Recipe recipe) {
    Map<String, Object> difficultyAndCategory = this.getDifficultyAndCategory(recipe.toDto());
    recipe.setDifficultyLevel(DifficultyLevel.convert((String) difficultyAndCategory.get("difficulty")));
  }


}
