package com.cookingapp.cookingapp.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.cookingapp.cookingapp.model.RecipeES;
import com.cookingapp.cookingapp.repo.RecipeESRepository;
import com.cookingapp.cookingapp.service.RecipeESService;
import com.cookingapp.cookingapp.util.ESUtil;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeESServiceImp implements RecipeESService {

  private final RecipeESRepository recipeESRepository;
  private final ElasticsearchClient elasticsearchClient;

  public RecipeES save(RecipeES recipeES){
    return recipeESRepository.save(recipeES);
  }

  public List<RecipeES> saveAll(List<RecipeES> recipeESList){
    return (List<RecipeES>) recipeESRepository.saveAll(recipeESList);
  }

  public void deleteAll() {
    // Specify a large page size to retrieve all documents in a single page
    Page<RecipeES> recipeESPage = recipeESRepository.findAll(PageRequest.of(0, Integer.MAX_VALUE));

    // Extract the content (List<RecipeES>) from the Page
    List<RecipeES> recipeESList = recipeESPage.getContent();

    for(RecipeES recipe: recipeESList){
      recipeESRepository.delete(recipe);
    }

  }
  public List<RecipeES> searchRecipe(String recipeName){
    SearchResponse<RecipeES> response = null;
    try {
      Supplier<Query> querySupplier = ESUtil.buildQueryForFieldAndValue("recipeName", recipeName, true);

      response = elasticsearchClient.search(q -> q.index("recipes")
          .query(querySupplier.get()), RecipeES.class);

      log.info("Elasticsearch response: {}", response.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return extractItemsFromResponse(response);
  }

  public List<RecipeES> extractItemsFromResponse(SearchResponse<RecipeES> response){
    return response
        .hits()
        .hits()
        .stream()
        .map(Hit::source)
        .toList();
  }
}