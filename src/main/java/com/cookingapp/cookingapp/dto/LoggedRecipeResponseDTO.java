package com.cookingapp.cookingapp.dto;

import com.cookingapp.cookingapp.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoggedRecipeResponseDTO {
  private Recipe recipe;
  private Boolean liked;
  private Boolean saved;

}