package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.RecipeTerm;
import com.cookingapp.cookingapp.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeTermRepository extends JpaRepository<RecipeTerm, Long> {

    RecipeTerm save(RecipeTerm recipeTerm);
    List<RecipeTerm> findRecipeTermsByRecipe(Recipe recipe);

    List<RecipeTerm> findRecipeTermsByTerm(Term term);
}
