package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.model.RecipeES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecipeESRepository extends ElasticsearchRepository<RecipeES, Long > {
    RecipeES save(RecipeES recipeES);

  @Override
  Page<RecipeES> findAll(Pageable pageable);
}
