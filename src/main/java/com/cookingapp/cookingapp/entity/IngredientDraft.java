package com.cookingapp.cookingapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ingredient_draft")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class IngredientDraft {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "recipe_draft_id")
  private RecipeDraft recipe;

  private String ingredient;

  private String amount;

  public Ingredient toIngredient(){
    Ingredient ingredientObject = new Ingredient();
    ingredientObject.setIngredient(ingredient);
    ingredientObject.setAmount(amount);
    return ingredientObject;
  }

}