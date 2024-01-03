package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.service.IngredientService;
import com.cookingapp.cookingapp.service.MemberService;
import com.cookingapp.cookingapp.service.RecipeESService;
import com.cookingapp.cookingapp.service.RecipeService;
import com.cookingapp.cookingapp.service.RecipeServiceFacade;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeServiceFacadeImp implements RecipeServiceFacade {

  private final RecipeService recipeService;

  private final IngredientService ingredientService;

  private final RecipeESService recipeESService;

  private final MemberService memberService;

  @Override
  public Recipe saveRecipe(Recipe recipe, List<Ingredient> ingredientList) {
    Recipe finalRecipe = recipeService.save(recipe);
    List<Ingredient> list = new ArrayList<>();
    for (Ingredient ingredient : ingredientList) {
      ingredient.setRecipe(finalRecipe);
      list.add(ingredient);
    }
    ingredientList = list;
    ingredientService.saveAll(ingredientList);
    recipeESService.save(recipe.toRecipeES());
    return recipe;
  }

  // to do member ekle
  @Override
  public Recipe saveRecipe(Recipe recipe, List<Ingredient> ingredientList, Member member) {
    Recipe finalRecipe = recipeService.save(recipe);
    List<Ingredient> list = new ArrayList<>();
    for (Ingredient ingredient : ingredientList) {
      ingredient.setRecipe(finalRecipe);
      list.add(ingredient);
    }
    ingredientList = list;
    ingredientService.saveAll(ingredientList);
    recipeESService.save(recipe.toRecipeES());
    return recipe;
  }


}
