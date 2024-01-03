package com.cookingapp.cookingapp.model;

import com.cookingapp.cookingapp.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "ingredients_index")
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientES {

  @Id
  private Long id;

  @Field(type = FieldType.Text)
  private String ingredient;

  @Field(type = FieldType.Text)
  private String amount;
}
