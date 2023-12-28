package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.SavedRecipe;
import com.cookingapp.cookingapp.repo.SavedRecipeRepository;
import com.cookingapp.cookingapp.service.RecipeService;
import com.cookingapp.cookingapp.service.SavedRecipeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavedRecipeServiceImp implements SavedRecipeService {

  private final SavedRecipeRepository savedRecipeRepository;

  private final RecipeService recipeService;

  @Override
  public SavedRecipe save(Long recipeId, Member member) {
    Recipe recipe = recipeService.getRecipeById(recipeId);
    SavedRecipe recipeToSave = new SavedRecipe();
    recipeToSave.setRecipe(recipe);
    recipeToSave.setMember(member);
    return savedRecipeRepository.save(recipeToSave);
  }

  @Override
  public void delete(Long recipeId, Member member) {
    Recipe recipe = recipeService.getRecipeById(recipeId);
    SavedRecipe savedRecipe = findSaveByRecipeAndMember(recipe, member);
    savedRecipeRepository.deleteSavedRecipeById(savedRecipe);
  }

  @Override
  public SavedRecipe findSaveByRecipeAndMember(Recipe recipe, Member member) {
    return savedRecipeRepository.findSavedRecipeByRecipeAndMember(recipe, member);
  }

  @Override
  public SavedRecipe findSaveByRecipeAndMember(Long recipeId, Member member) {
    Recipe recipe = recipeService.getRecipeById(recipeId);
    return savedRecipeRepository.findSavedRecipeByRecipeAndMember(recipe, member);
  }

  @Override
  public List<Recipe> getSavedRecipesByMember(Member member) {
    return savedRecipeRepository.getSavedRecipesByMember(member);
  }

}
