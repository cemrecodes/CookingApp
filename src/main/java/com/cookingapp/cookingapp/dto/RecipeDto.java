package com.cookingapp.cookingapp.dto;

import com.cookingapp.cookingapp.entity.Category;
import com.cookingapp.cookingapp.entity.DifficultyLevel;
import com.cookingapp.cookingapp.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDto {

    private Long id;
    private Long memberId;
    private String memberName;
    private String imageUrl;
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
    private Long likeCount;
    private boolean liked;
    private boolean saved;

    public String toStringForAI(){
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
        recipe.setRecipeName(recipeName);
        recipe.setCookingTime(cookingTime);
        recipe.setPreparationTime(preparationTime);
        recipe.setServesFor(servesFor);
        recipe.setDifficultyLevel(difficultyLevel != null ? DifficultyLevel.convert(difficultyLevel) : null);
        recipe.setCategory(category != null ? Category.convert(category) : null);
        // recipe.setIngredients(createIngredientsString(ingredients));
        recipe.setInstructions(createInstructionsString(instructions));
        recipe.setLikeCount(likeCount);
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
