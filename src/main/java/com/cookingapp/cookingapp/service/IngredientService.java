package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.repo.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    
    public List<Ingredient> getIngredientsByRecipeId(Long id) {
        return ingredientRepository.getByRecipeId(id);
    }

    
    public List<Ingredient> saveAll(List<Ingredient> ingredientList) {
        ingredientRepository.saveAll(ingredientList);
      return ingredientList;
    }

}
