package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.SavedRecipe;
import java.util.List;

public interface SavedRecipeService {

  SavedRecipe save(Long recipeId, Member member);

  void delete(Long recipeId, Member member);

  SavedRecipe findSaveByRecipeAndMember(Recipe recipe, Member member);

  SavedRecipe findSaveByRecipeAndMember(Long recipeId, Member member);

  List<Recipe> getSavedRecipesByMember(Member member);
}
