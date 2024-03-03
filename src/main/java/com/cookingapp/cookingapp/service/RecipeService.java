package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.RecipeWithLikesAndSaves;
import com.cookingapp.cookingapp.entity.Recipe;

import com.cookingapp.cookingapp.repo.IRecipeResponse;
import java.util.List;

public interface RecipeService {

    Recipe save(Recipe recipe);

    Recipe getRecipeById(Long id);

    List<Recipe> getAllRecipe();

    List<IRecipeResponse> getAllRecipeLoggedIn(Long memberId);

    List<RecipeWithLikesAndSaves> getAllRecipeLoggedIn2(Long memberId);

    List<Recipe> getRecipeByName(String recipe);

    List<Recipe> getRecipesByCategory(String category);

    void deleteRecipeById(Long id);

    void chooseDailyRandomRecipe();

    Recipe getDailyRandomRecipe();

}
