package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.dto.RecipeProjection;
import com.cookingapp.cookingapp.dto.RecipeWithLikesAndSaves;
import com.cookingapp.cookingapp.entity.Category;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.repo.IRecipeResponse;
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
    // todo make this bean
    private final Random random = new Random();

    @Override
    public Recipe save(Recipe recipe) {
        return this.recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return this.recipeRepository.getRecipeById(id);
    }

    @Override
    public List<RecipeProjection> getRecipeProjection() {
        return this.recipeRepository.getAll();
    //    return null;
    }

    // @Override
    public RecipeProjection getRecipeById2(Long id) {
        // return this.recipeRepository.getDtoById(id);
        return null;
    }


    @Override
    public List<Recipe> getAllRecipe(){
        return this.recipeRepository.findAll();
    }

    @Override
    public List<IRecipeResponse> getAllRecipeLoggedIn(Long memberId) {
        List<IRecipeResponse> responses = this.recipeRepository.getRecipesLoggedIn(memberId);
        return responses;
    }

    @Override
    public List<RecipeWithLikesAndSaves> getAllRecipeLoggedIn2(Long memberId) {
        List<RecipeWithLikesAndSaves> responses = this.recipeRepository.getRecipesLoggedIn2(memberId);
        return responses;
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
            int randomIndex = random.nextInt(recipeList.size());
            dailyRandomRecipe = recipeList.get(randomIndex);
        }
    }

    @Override
    public Recipe getDailyRandomRecipe() {
        return dailyRandomRecipe;
    }

    @Override
    public void scoreRecipe(Long recipeId, Double score) {
        Double oldScore = this.getRecipeById(recipeId).getScore();

    }
}
