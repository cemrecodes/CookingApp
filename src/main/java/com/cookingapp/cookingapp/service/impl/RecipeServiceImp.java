package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.repo.RecipeRepository;
import com.cookingapp.cookingapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImp implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;


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
}
