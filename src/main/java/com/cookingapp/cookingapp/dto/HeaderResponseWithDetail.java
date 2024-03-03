package com.cookingapp.cookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HeaderResponseWithDetail {

  private RecipeDto recipeDto;
  private boolean liked;
  private boolean saved;

}
