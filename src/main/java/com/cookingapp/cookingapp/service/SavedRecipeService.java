package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.SavedRecipe;
import com.cookingapp.cookingapp.repo.SavedRecipeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavedRecipeService {

  private final SavedRecipeRepository savedRecipeRepository;

  private final RecipeService recipeService;

  
  public SavedRecipe save(Long recipeId, Member member) {
    Recipe recipe = recipeService.getRecipeById(recipeId);
    SavedRecipe recipeToSave = new SavedRecipe();
    recipeToSave.setRecipe(recipe);
    recipeToSave.setMember(member);
    return savedRecipeRepository.save(recipeToSave);
  }

  
  public void delete(Long recipeId, Member member) {
    Recipe recipe = recipeService.getRecipeById(recipeId);
    SavedRecipe savedRecipe = findSaveByRecipeAndMember(recipe, member);
    savedRecipeRepository.deleteSavedRecipeById(savedRecipe);
  }

  
  public SavedRecipe findSaveByRecipeAndMember(Recipe recipe, Member member) {
    return savedRecipeRepository.findSavedRecipeByRecipeAndMember(recipe, member);
  }

  
  public SavedRecipe findSaveByRecipeAndMember(Long recipeId, Member member) {
    Recipe recipe = recipeService.getRecipeById(recipeId);
    return savedRecipeRepository.findSavedRecipeByRecipeAndMember(recipe, member);
  }

  
  public List<Recipe> getSavedRecipesByMember(Member member) {
    return savedRecipeRepository.getSavedRecipesByMember(member);
  }

}
