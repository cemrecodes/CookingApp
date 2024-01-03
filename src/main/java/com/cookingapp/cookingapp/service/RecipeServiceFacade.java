package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import java.util.List;

public interface RecipeServiceFacade {

  Recipe saveRecipe(Recipe recipe, List<Ingredient> ingredientList);

  Recipe saveRecipe(Recipe recipe, List<Ingredient> ingredientList, Member member);


}
