package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Category;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.repo.RecipeRepository;
import com.cookingapp.cookingapp.service.RecipeService;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImp implements RecipeService {

    private final RecipeRepository recipeRepository;
    private Recipe dailyRandomRecipe;

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

    @Override
    public void chooseDailyRandomRecipe() {
        List<Recipe> recipeList = this.getAllRecipe();
        if(!recipeList.isEmpty()){
            Random random = new Random();
            int randomIndex = random.nextInt(recipeList.size());
            dailyRandomRecipe = recipeList.get(randomIndex);
        }
    }

    @Override
    public Recipe getDailyRandomRecipe() {
        return dailyRandomRecipe;
    }
}
