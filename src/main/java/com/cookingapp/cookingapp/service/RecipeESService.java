package com.cookingapp.cookingapp.service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.cookingapp.cookingapp.model.RecipeES;
import java.util.List;

public interface RecipeESService{

  RecipeES save(RecipeES recipeES);

  List<RecipeES> saveAll(List<RecipeES> recipeESList);

  void deleteAll();

  List<RecipeES> searchRecipe(String recipeName);

  List<RecipeES> extractItemsFromResponse(SearchResponse<RecipeES> response);

}
