package com.cookingapp.cookingapp.response;

import lombok.Data;

@Data
public class RecipeHeaderResponse {

  private Long id;
  private String imageUrl;
  private String image;
  private String recipeName;
  private String totalTime;
  private String servesFor;
  private String difficultyLevel;
  private String category;
  private Double score;
  private Long likeCount;

}
