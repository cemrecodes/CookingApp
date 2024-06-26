package com.cookingapp.cookingapp.model;

import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.entity.DifficultyLevel;
import com.cookingapp.cookingapp.response.RecipeHeaderResponse;
import com.cookingapp.cookingapp.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@Document(indexName = "recipes")
@JsonIgnoreProperties(ignoreUnknown = true)
@Setting(settingPath = "static/es-settings.json")
public class RecipeES {

  @Id
  private Long id;

  @Field(type = FieldType.Keyword)
  private String imageUrl;

  @Field(type = FieldType.Text, index = false, store = true)
  private String image;

  @Field(type = FieldType.Text)
  private String recipeName;

  @Field(type = FieldType.Keyword)
  private String cookingTime;

  @Field(type = FieldType.Keyword)
  private String preparationTime;

  @Field(type = FieldType.Keyword)
  private String totalTime;

  @Field(type = FieldType.Text)
  private String servesFor;

  @Field(type = FieldType.Keyword)
  private String difficultyLevel;

  @Field(type = FieldType.Keyword)
  private String category;

  private List<IngredientES> ingredients;

  private Long likeCount;


  public RecipeDto toDto(){
    RecipeDto recipeDto = new RecipeDto();
    recipeDto.setId(id);
    recipeDto.setImageUrl(imageUrl);
    recipeDto.setRecipeName(recipeName);
    recipeDto.setCookingTime(cookingTime);
    recipeDto.setPreparationTime(preparationTime);
    recipeDto.setTotalTime(totalTime);
    recipeDto.setServesFor(servesFor);
    recipeDto.setDifficultyLevel(difficultyLevel);
    recipeDto.setCategory(category);
    recipeDto.setLikeCount(likeCount);
    return recipeDto;
  }

  public RecipeHeaderResponse toHeaderResponse(boolean liked) {
    RecipeHeaderResponse response = new RecipeHeaderResponse();
    response.setId(id);
    response.setImageUrl(imageUrl);
    response.setImage(image);
    response.setRecipeName(recipeName);
    response.setTotalTime(totalTime);
    response.setServesFor(servesFor);
    response.setDifficultyLevel(Util.translateDifficultyLevelToTurkish(DifficultyLevel.valueOf(difficultyLevel)));
    response.setCategory(Util.translateCategoryToTurkish(category));
    response.setLikeCount(likeCount);
    response.setLiked(liked);
    return response;
  }
}
