package com.cookingapp.cookingapp.dto;

import com.cookingapp.cookingapp.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecipeWithLikesAndSaves {

  private Recipe recipe;
  private boolean liked;
  private boolean saved;

}