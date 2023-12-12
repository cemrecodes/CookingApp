package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Ingredient save(Ingredient ingredient);

    List<Ingredient> getByRecipeId(Long recipeId);


}
