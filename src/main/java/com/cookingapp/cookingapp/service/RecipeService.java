package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Recipe;

import java.util.List;

public interface RecipeService {

    Recipe save(Recipe recipe);

    Recipe getRecipeById(Long id);

    List<Recipe> getAllRecipe();

    List<Recipe> getRecipeByName(String recipe);

    List<Recipe> getRecipesByCategory(String category);

    void deleteRecipeById(Long id);

    void chooseDailyRandomRecipe();
    Recipe getDailyRandomRecipe();

}
