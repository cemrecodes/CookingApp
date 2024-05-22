package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.RecipeProjection;
import com.cookingapp.cookingapp.dto.RecipeWithLikesAndSaves;
import com.cookingapp.cookingapp.entity.Recipe;
import java.util.List;

public interface RecipeService {

    Recipe save(Recipe recipe);

    Recipe getRecipeById(Long id);

    // RecipeProjection getRecipeById2(Long id);

    List<RecipeProjection> getRecipeProjection();

    List<Recipe> getAllRecipe();

    // List<IRecipeResponse> getAllRecipeLoggedIn(Long memberId);

    List<RecipeWithLikesAndSaves> getAllRecipeLoggedIn(Long memberId);

    List<Recipe> getRecipeByName(String recipe);

    List<Recipe> getRecipesByCategory(String category);

    void deleteRecipeById(Long id);

    void chooseDailyRandomRecipe();

    Recipe getDailyRandomRecipe();

}
