package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.dto.IngredientDto;
import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.repo.IngredientRepository;
import com.cookingapp.cookingapp.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Ingredient> processIngredientsHashMap(Map<Integer, ArrayList<String>> data, Recipe recipe) {
        List<Ingredient> ingredients = new ArrayList<>();

        for (Map.Entry<Integer, ArrayList<String>> entry : data.entrySet()) {
            ArrayList<String> values = entry.getValue();

            if (values.size() >= 2) {
                String ingredientName = values.get(0);
                String amount = values.get(1);

                Ingredient ingredient = new Ingredient();
                ingredient.setRecipe(recipe);
                ingredient.setIngredient(ingredientName);
                ingredient.setAmount(amount);

                ingredients.add(ingredient);
            }
        }

        return ingredients;
    }
}
