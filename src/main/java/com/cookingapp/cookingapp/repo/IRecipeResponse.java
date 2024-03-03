package com.cookingapp.cookingapp.repo;

public interface IRecipeResponse {
  Recipe getRecipe();
  Boolean getLiked();
  Boolean getSaved();

  interface Recipe {
    Long getId();
    String getImageUrl();
    String getRecipeName();
    String getCookingTime();
    String getPreparationTime();
    String getServesFor();
    String getDifficultyLevel();
    String getCategory();
  }
}
