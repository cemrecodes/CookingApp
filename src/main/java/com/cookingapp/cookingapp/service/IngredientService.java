package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IngredientService {

    Ingredient save(Ingredient ingredient);

    List<Ingredient> getIngredientsByRecipeId(Long id);

}
