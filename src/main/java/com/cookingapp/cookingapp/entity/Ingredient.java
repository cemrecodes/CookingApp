package com.cookingapp.cookingapp.entity;

import com.cookingapp.cookingapp.dto.IngredientDto;
import com.cookingapp.cookingapp.model.IngredientES;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "ingredient")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @JsonBackReference
    private Recipe recipe;

    private String ingredient;

    private String amount;

    public IngredientDto toDto(){
            IngredientDto ingredientDto = new IngredientDto();
            ingredientDto.setIngredient(ingredient);
            ingredientDto.setAmount(amount);
            return ingredientDto;
    }

  public IngredientES toIngredientES() {
      IngredientES ingredientES = new IngredientES();
      ingredientES.setId(id);
      ingredientES.setIngredient(ingredient);
      ingredientES.setAmount(amount);
      return ingredientES;
  }
}
