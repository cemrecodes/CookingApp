package com.cookingapp.cookingapp.dto;

import com.cookingapp.cookingapp.entity.Category;
import com.cookingapp.cookingapp.entity.DifficultyLevel;
import com.cookingapp.cookingapp.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDto {

    private Long id;
    private String imageUrl;
    private String recipeName;
    private String cookingTime;
    private String preparationTime;
    private String totalTime;
    private int servesFor;
    private String difficultyLevel;
    private String category;
    private HashMap<Integer, ArrayList<String>> ingredients;
    private HashMap<Integer, String> instructions;
    private OffsetDateTime createTime;
    private HashMap<Integer, ArrayList<String>> terms;
    private int score;

    public String toStringForChatGpt(){
        StringBuilder specialStringBuilder = new StringBuilder();
        for (Map.Entry<Integer, String> entry : instructions.entrySet()) {
            specialStringBuilder.append(entry.getValue()).append(". ");
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
        recipe.setDifficultyLevel(DifficultyLevel.convert(difficultyLevel));
        recipe.setCategory(Category.convert(category));
        recipe.setIngredients(createIngredientsString(ingredients));
        recipe.setInstructions(createInstructionsString(instructions));
        return recipe;
    }

    private String createIngredientsString(HashMap<Integer, ArrayList<String>> ingredients) {
        StringBuilder resultBuilder = new StringBuilder();

        for (ArrayList<String> ingredientList : ingredients.values()) {
            for (String ingredient : ingredientList) {
                resultBuilder.append(ingredient).append(" ");
            }
            resultBuilder.append(",");
        }

        if (!resultBuilder.isEmpty()) {
            resultBuilder.setLength(resultBuilder.length() - 1);
        }

        return resultBuilder.toString();
    }

    private String createInstructionsString(HashMap<Integer, String> methodList) {
        StringBuilder resultBuilder = new StringBuilder();

        for (String method : methodList.values()) {
            resultBuilder.append(method).append(" ");
        }

        return resultBuilder.toString();
    }

}
