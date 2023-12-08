package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Recipe save(Recipe recipe);
    Recipe getRecipeById(Long id);
    List<Recipe> findAll();

    Recipe findByRecipeName(String name);

}
