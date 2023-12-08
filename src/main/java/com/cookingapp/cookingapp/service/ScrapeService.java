package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.RecipeDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface ScrapeService {

    RecipeDto scrapeAndCreateNewRecipe(String foodName);
    String searchRecipeUrl(String foodName);
    String scrapeUrl(String url, String permaLink);
    RecipeDto getRecipe(String script);

    /*
    private HashMap<Integer, ArrayList<String>> getIngredientsMap(List<List<String>> recipeIngredients);

    private HashMap<Integer, String> getInstructionsMap(List<List<String>> recipeInstructions);
    private Map<String, Object> getDifficultyAndTerms(RecipeDto recipe);
    private String getDifficulty(RecipeDto recipe);

     */
    Map<String, Object> getTerms(RecipeDto recipe);
    HashMap<Integer, ArrayList<String>> getTermsMap(Map<String, Object> terms);

    // private String getTotalTime(String cookingTime, String preparationTime);
}
