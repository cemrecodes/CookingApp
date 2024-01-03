package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Ingredient;

import java.util.List;

public interface IngredientService {

    Ingredient save(Ingredient ingredient);

    List<Ingredient> getIngredientsByRecipeId(Long id);

    void saveAll(List<Ingredient> ingredientList);

}
