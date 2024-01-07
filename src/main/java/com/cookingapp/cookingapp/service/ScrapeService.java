package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.RecipeDto;

import com.cookingapp.cookingapp.entity.Recipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface ScrapeService {

    Recipe scrapeAndCreateNewRecipe(String foodName);
    String searchRecipeUrl(String foodName);
    HashMap<String, Object> scrapeUrl(String url, String permaLink);
    RecipeDto getRecipe(HashMap<String, Object> recipeMap);
    Map<String, Object> getTerms(RecipeDto recipe);
    HashMap<Integer, ArrayList<String>> getTermsMap(Map<String, Object> terms);
  }
