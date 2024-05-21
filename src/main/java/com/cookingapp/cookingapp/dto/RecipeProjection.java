package com.cookingapp.cookingapp.dto;

import com.cookingapp.cookingapp.entity.Ingredient;

public interface RecipeProjection {
    Long getId();

    String getRecipeName();

    Ingredient getIngredients();
    /*
    IngredientProjection getIngredient();

    interface IngredientProjection {
        Long getId();
        String getIngredient();
    }
     */
}