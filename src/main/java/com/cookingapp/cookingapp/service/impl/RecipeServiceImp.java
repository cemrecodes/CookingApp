package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.repo.RecipeRepository;
import com.cookingapp.cookingapp.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImp implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Override
    public Recipe save(Recipe recipe) {
        return this.recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return null;
    }

    @Override
    public List<Recipe> getAllRecipe(){
        return this.recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipeByName(String name) {
        return this.recipeRepository.findByRecipeName(name);
    }
}
