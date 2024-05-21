package com.cookingapp.cookingapp.entity;
import com.cookingapp.cookingapp.dto.IngredientDto;
import com.cookingapp.cookingapp.dto.InstructionDto;
import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.model.IngredientES;
import com.cookingapp.cookingapp.model.RecipeES;
import com.cookingapp.cookingapp.response.RecipeHeaderResponse;
import com.cookingapp.cookingapp.util.Util;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
// @ToString
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String imageUrl;

    @Lob
    private String image;

    @Column(length = 100)
    private String recipeName;

    @Column(length = 20)
    private String cookingTime;

    @Column(length = 20)
    private String preparationTime;

    @Column(length = 20)
    private String totalTime;

    @Column(length = 20)
    private String servesFor;

    @Enumerated
    private DifficultyLevel difficultyLevel;

    @Enumerated
    private Category category;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Ingredient> ingredients;

    @Lob
    private String instructions;

    @Column(columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    private Double score;

    private boolean termsAdded;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes;

    @PrePersist
    public void prePersist() {
        this.createDate = LocalDateTime.now();
        if(score == null){
            this.score = 0.0;
        }
        this.totalTime = Util.getTotalTime(this.cookingTime, this.preparationTime);
    }

    public RecipeDto toDto(){
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(id);
        recipeDto.setImageUrl(imageUrl);
        recipeDto.setRecipeName(recipeName);
        recipeDto.setCookingTime(cookingTime);
        recipeDto.setPreparationTime(preparationTime);
        recipeDto.setTotalTime(totalTime);
        recipeDto.setServesFor(servesFor);
        recipeDto.setDifficultyLevel(difficultyLevel != null ? DifficultyLevel.toString(difficultyLevel) : null);
        recipeDto.setCategory(category != null ? Category.toString(category) : null);
        if( ingredients != null ) {
            List<IngredientDto> ingredientDtos = ingredients.stream()
                .map(Ingredient::toDto)
                .toList();
            recipeDto.setIngredients(new ArrayList<>(ingredientDtos));
        }
        else{
            recipeDto.setIngredients(null);
        }
        recipeDto.setInstructions(this.convertInstructions());
        recipeDto.setScore(score);
        return recipeDto;
    }

    public RecipeHeaderResponse toHeaderResponse(){
        RecipeHeaderResponse response = new RecipeHeaderResponse();
        response.setId(id);
        response.setImageUrl(imageUrl);
        response.setImage(image);
        response.setRecipeName(recipeName);
        response.setTotalTime(totalTime);
        response.setServesFor(servesFor);
        response.setDifficultyLevel(DifficultyLevel.toString(difficultyLevel));
        response.setCategory( Category.toString(category));
        response.setScore(score);
        return response;
    }

    private ArrayList<InstructionDto> convertInstructions(){
        ArrayList<InstructionDto> instructionArray = new ArrayList<>();
        String[] split = instructions.split("\n");

        for (String instruction : split) {
            InstructionDto instructionDto = new InstructionDto();
            if(!instruction.equals(" ")) {
                instructionDto.setInstruction(instruction);
                instructionDto.setTime(Util.findPrepOrCookTime(instruction));
                instructionArray.add(instructionDto);
            }
        }

        return instructionArray;
    }

    public RecipeES toRecipeES(){
        RecipeES recipeES = new RecipeES();
        recipeES.setId(id);
        recipeES.setImageUrl(imageUrl);
        recipeES.setImage(image);
        recipeES.setRecipeName(recipeName);
        recipeES.setCookingTime(cookingTime);
        recipeES.setPreparationTime(preparationTime);
        recipeES.setServesFor(servesFor);
        recipeES.setDifficultyLevel(difficultyLevel.toString());
        recipeES.setCategory(category.toString());
        List<IngredientES> ingredientESList = new ArrayList<>();
        for(Ingredient ingredient: ingredients){
            ingredientESList.add(ingredient.toIngredientES());
        }
        recipeES.setIngredients(ingredientESList);
        recipeES.setScore(score);
        recipeES.setTermsAdded(termsAdded);
        return recipeES;
    }

}