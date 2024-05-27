package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.InstructionDto;
import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.dto.RecipeProjection;
import com.cookingapp.cookingapp.dto.RecipeWithLikesAndSaves;
import com.cookingapp.cookingapp.entity.Category;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.model.RecipeES;
import com.cookingapp.cookingapp.repo.RecipeRepository;
import com.cookingapp.cookingapp.response.RecipeHeaderResponse;
import java.util.ArrayList;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private Recipe dailyRandomRecipe;
    // todo make this bean
    private final GeminiService geminiService;
    private final Random random = new Random();

    
    public Recipe save(Recipe recipe) {
        return this.recipeRepository.save(recipe);
    }

    
    public Recipe getRecipeById(Long id) {
        return this.recipeRepository.getRecipeById(id);
    }

    
    public List<RecipeProjection> getRecipeProjection() {
        return this.recipeRepository.getAll();
    //    return null;
    }

    // 
    public RecipeProjection getRecipeById2(Long id) {
        // return this.recipeRepository.getDtoById(id);
        return null;
    }


    
    public List<Recipe> getAllRecipe(){
        return this.recipeRepository.findAll();
    }

    /*
    
    public List<IRecipeResponse> getAllRecipeLoggedIn(Long memberId) {
        List<IRecipeResponse> responses = this.recipeRepository.getRecipesLoggedIn(memberId);
        return responses;
    }
    */

    
    public List<RecipeWithLikesAndSaves> getAllRecipeLoggedIn(Long memberId) {
        List<RecipeWithLikesAndSaves> responses = this.recipeRepository.getRecipesLoggedIn2(memberId);
        return responses;
    }

    
    public List<Recipe> getRecipeByName(String name) {
        return this.recipeRepository.findByRecipeName(name);
    }

    
    public List<Recipe> getRecipesByCategory(String category) {
        return this.recipeRepository.findByCategory(Category.convert(category));
    }

    
    @Transactional
    public void deleteRecipeById(Long id) {
        this.recipeRepository.deleteRecipeById(id);
    }

    
    public void chooseDailyRandomRecipe() {
        List<Recipe> recipeList = this.getAllRecipe();
        if(!recipeList.isEmpty()){
            int randomIndex = random.nextInt(recipeList.size());
            dailyRandomRecipe = recipeList.get(randomIndex);
        }
    }

    
    public Recipe getDailyRandomRecipe() {
        return dailyRandomRecipe;
    }

    public String explainInstruction(Long recipeId, Long instructionIndex) {
        RecipeDto recipe = this.recipeRepository.getRecipeById(recipeId).toDto(false, false);
        int i = 0;
        String explanation = "";
        List<InstructionDto> instructions = recipe.getInstructions();
        for(InstructionDto instruction : instructions){
            if(i == instructionIndex.intValue()){
                explanation = this.geminiService.explainInstruction(instruction.getInstruction());
                break;
            }
            i++;
        }
        explanation = explanation.replace("\n", " ");
        explanation = explanation.replace("**", "");
        return explanation;
    }

    public String explainInstruction(String instruction) {
        String explanation = this.geminiService.explainInstruction(instruction);
        explanation = explanation.replace("\n", " ");
        explanation = explanation.replace("*", "");
        return explanation;
    }

    public List<RecipeDto> getRecipeListWithLikesAndSaves(List<Recipe> recipeList, List<Long> likedRecipeIds, List<Long> savedRecipeIds) {
        return recipeList.stream()
                .map(recipe -> {
                    boolean liked = likedRecipeIds != null && !likedRecipeIds.isEmpty() && likedRecipeIds.contains(recipe.getId());
                    boolean saved = savedRecipeIds != null && !savedRecipeIds.isEmpty() && savedRecipeIds.contains(recipe.getId());
                    return recipe.toDto(liked, saved);
                })
                .toList();
    }

    public List<RecipeHeaderResponse> getRecipeHeaderResponseWithLikes(List<Recipe> recipeList, List<Long> likedRecipeIds) {
        return recipeList.stream()
            .map(recipe -> {
                boolean liked = likedRecipeIds != null && !likedRecipeIds.isEmpty() && likedRecipeIds.contains(recipe.getId());
                return recipe.toHeaderResponse(liked);
            })
            .toList();
    }

    public List<RecipeHeaderResponse> getRecipeHeaderResponseWithLikesES(List<RecipeES> recipeList, List<Long> likedRecipeIds) {
        return recipeList.stream()
            .map(recipe -> {
                boolean liked = likedRecipeIds != null && !likedRecipeIds.isEmpty() && likedRecipeIds.contains(recipe.getId());
                return recipe.toHeaderResponse(liked);
            })
            .toList();
    }

}
