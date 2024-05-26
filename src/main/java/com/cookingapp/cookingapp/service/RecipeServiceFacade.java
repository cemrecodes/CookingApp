package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.entity.Category;
import com.cookingapp.cookingapp.entity.DifficultyLevel;
import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.util.Util;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeServiceFacade {

  private final RecipeService recipeService;
  private final IngredientService ingredientService;
  private final RecipeESService recipeESService;
  private final RecipeMemberService recipeMemberService;
  private final GeminiService geminiService;
  private final ImageUploadService imageUploadService;

  
  public Recipe saveRecipe(Recipe recipe, List<Ingredient> ingredientList) {
    setDifficultyAndCategory(recipe);
    recipe.setLikeCount(0L);
    recipe.setCreateDate(LocalDateTime.now());
    recipe.setTotalTime(Util.getTotalTime(recipe.getCookingTime(), recipe.getPreparationTime()));
    Recipe finalRecipe = recipeService.save(recipe);
    String imageUrl = imageUploadService.uploadImageFromUrl(finalRecipe.getId(), recipe.getImageUrl());
    recipe.setImageUrl(imageUrl);
    finalRecipe = recipeService.save(recipe);
    List<Ingredient> list = new ArrayList<>();
    for (Ingredient ingredient : ingredientList) {
      ingredient.setRecipe(finalRecipe);
      list.add(ingredient);
    }
    recipe.setIngredients( ingredientService.saveAll(list) );
    recipeESService.save(recipe.toRecipeES());
    return recipe;
  }

  
  public Recipe saveRecipe(Recipe recipe, List<Ingredient> ingredientList, Member member) {
    setDifficulty(recipe);
    recipe.setLikeCount(0L);
    recipe.setCreateDate(LocalDateTime.now());
    recipe.setTotalTime(Util.getTotalTime(recipe.getCookingTime(), recipe.getPreparationTime()));
    Recipe finalRecipe = recipeService.save(recipe);
    byte[] imageBytes = Base64.getDecoder().decode(finalRecipe.getImage());
    String imageUrl = imageUploadService.uploadImage(finalRecipe.getId(), imageBytes);
    finalRecipe.setImageUrl(imageUrl);
    List<Ingredient> list = new ArrayList<>();
    for (Ingredient ingredient : ingredientList) {
      ingredient.setRecipe(finalRecipe);
      list.add(ingredient);
    }
    ingredientList = list;
    ingredientList = ingredientService.saveAll(ingredientList);
    finalRecipe.setIngredients(ingredientList);
    recipeESService.save(finalRecipe.toRecipeES());
    recipeMemberService.save(finalRecipe, member);
    return finalRecipe;
  }

  /* gets difficulty and category of recipe from ai */
  private Map<String, Object> getDifficultyAndCategory(RecipeDto recipe) {
    String aiResponse = geminiService.getDifficultyLevelAndCategory(recipe);
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
