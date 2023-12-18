package com.cookingapp.cookingapp.dto;

import com.cookingapp.cookingapp.entity.Ingredient;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientDto {

  private String ingredient;

  private String amount;

  public Ingredient toIngredient(){
    Ingredient ingredientObject = new Ingredient();
    ingredientObject.setIngredient(ingredient);
    ingredientObject.setAmount(amount);
    return ingredientObject;
  }

}
