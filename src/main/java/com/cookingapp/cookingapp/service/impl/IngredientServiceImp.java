package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.repo.IngredientRepository;
import com.cookingapp.cookingapp.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImp implements IngredientService {

    private final IngredientRepository ingredientRepository;

    @Override
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public List<Ingredient> getIngredientsByRecipeId(Long id) {
        return ingredientRepository.getByRecipeId(id);
    }

    @Override
    public List<Ingredient> saveAll(List<Ingredient> ingredientList) {
        ingredientRepository.saveAll(ingredientList);
      return ingredientList;
    }

}
