package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Recipe;

import java.util.List;

public interface RecipeService {

    public Recipe save(Recipe recipe);

    public Recipe getRecipeById(Long id);

    public List<Recipe> getAllRecipe();
}
