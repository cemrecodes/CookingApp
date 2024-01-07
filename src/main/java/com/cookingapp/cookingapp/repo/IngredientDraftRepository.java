package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.IngredientDraft;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientDraftRepository extends JpaRepository<IngredientDraft, Long> {

  IngredientDraft save(IngredientDraft ingredient);

  List<IngredientDraft> getByRecipeId(Long recipeId);


}
