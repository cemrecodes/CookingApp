package com.cookingapp.cookingapp.model;

import com.cookingapp.cookingapp.entity.Recipe;
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

  @Field(type = FieldType.Text)
  private String recipeName;

  @Field(type = FieldType.Text, index = false, store = true)
  private String image;

  @Field(type = FieldType.Keyword)
  private String cookingTime;

  @Field(type = FieldType.Keyword)
  private String preparationTime;

  @Field(type = FieldType.Text)
  private String servesFor;

  @Field(type = FieldType.Keyword)
  private String difficultyLevel;

  @Field(type = FieldType.Keyword)
  private String category;

  private List<IngredientES> ingredients;

  @Field(type = FieldType.Double)
  private Double score;

  @Field(type = FieldType.Boolean)
  private boolean termsAdded;


}
