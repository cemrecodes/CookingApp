package com.cookingapp.cookingapp.dto;

import com.cookingapp.cookingapp.entity.Category;
import com.cookingapp.cookingapp.entity.DifficultyLevel;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.RecipeDraft;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDto {

    private Long id;
    private String imageUrl;
    private String image;
    private String recipeName;
    private String cookingTime;
    private String preparationTime;
    private String totalTime;
    private String servesFor;
    private String difficultyLevel;
    private String category;
    private ArrayList<IngredientDto> ingredients;
    private ArrayList<InstructionDto> instructions;
    private OffsetDateTime createTime;
    private HashMap<Integer, ArrayList<String>> terms;
    private Double score;

    public String toStringForChatGpt(){
        StringBuilder specialStringBuilder = new StringBuilder();
        for (InstructionDto instructionDto : instructions) {
            specialStringBuilder.append(instructionDto.getInstruction()).append(". ");
        }

        return recipeName + " Pişirme Süresi: " + cookingTime + " Hazırlama Süresi: " + preparationTime +
               " " + servesFor + " kişilik Yapılışı: " + specialStringBuilder;
    }

    public Recipe convertToRecipe(){
        Recipe recipe = new Recipe();
        recipe.setImageUrl(imageUrl);
        recipe.setImage(image);
        recipe.setRecipeName(recipeName);
        recipe.setCookingTime(cookingTime);
        recipe.setPreparationTime(preparationTime);
        recipe.setServesFor(servesFor);
        recipe.setDifficultyLevel(difficultyLevel != null ? DifficultyLevel.convert(difficultyLevel) : null);
        recipe.setCategory(category != null ? Category.convert(category) : null);
        // recipe.setIngredients(createIngredientsString(ingredients));
        recipe.setInstructions(createInstructionsString(instructions));
        recipe.setTermsAdded(false);
        recipe.setScore(score);
        return recipe;
    }

    public RecipeDraft convertToRecipeDraft(){
        RecipeDraft recipe = new RecipeDraft();
        recipe.setImage(image);
        recipe.setRecipeName(recipeName);
        recipe.setCookingTime(cookingTime);
        recipe.setPreparationTime(preparationTime);
        recipe.setServesFor(servesFor);
        recipe.setCategory(category != null ? Category.convert(category) : null);
        recipe.setInstructions(createInstructionsString(instructions));
        return recipe;
    }


    private String createInstructionsString(ArrayList<InstructionDto> instructionArray){
        StringBuilder resultBuilder = new StringBuilder();

        for (InstructionDto instructionDto: instructionArray) {
            resultBuilder.append(instructionDto.getInstruction()).append("\n");
        }

        return resultBuilder.toString();
    }

}
