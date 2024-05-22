package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.RecipeTerm;
import com.cookingapp.cookingapp.entity.Term;
import com.cookingapp.cookingapp.repo.RecipeRepository;
import com.cookingapp.cookingapp.repo.RecipeTermRepository;
import com.cookingapp.cookingapp.repo.TermRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RecipeTermService {

    private final RecipeTermRepository recipeTermRepository;
    private final TermRepository termRepository;
    private final RecipeRepository recipeRepository;

    
    @Transactional
    public void addTermToRecipe(Long recipeId, String termWord, String termDefinition) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));

        Term term = new Term();
        term.setTerm(termWord);
        term.setDefinition(termDefinition);

        RecipeTerm recipeTerm = new RecipeTerm();
        recipeTerm.setRecipe(recipe);
        recipeTerm.setTerm(term);
        recipeTermRepository.save(recipeTerm);
    }
}