package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Category;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.repo.RecipeRepository;
import com.cookingapp.cookingapp.service.RecipeService;
import com.cookingapp.cookingapp.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return this.recipeRepository.getRecipeById(id);
    }

    @Override
    public List<Recipe> getAllRecipe(){
        return this.recipeRepository.findAll();
    }

    @Override
    public List<Recipe> getRecipeByName(String name) {
        return this.recipeRepository.findByRecipeName(name);
    }

    @Override
    public List<Recipe> getRecipesByCategory(String category) {
        return this.recipeRepository.findByCategory(Category.convert(category));
    }

    @Override
    @Transactional
    public void deleteRecipeById(Long id) {
        this.recipeRepository.deleteRecipeById(id);
    }
}
