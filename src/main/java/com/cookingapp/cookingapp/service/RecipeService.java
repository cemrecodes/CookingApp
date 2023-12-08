package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Recipe;

import java.util.List;

public interface RecipeService {

    Recipe save(Recipe recipe);

    Recipe getRecipeById(Long id);

    List<Recipe> getAllRecipe();

    Recipe getRecipeByName(String recipe);


}
