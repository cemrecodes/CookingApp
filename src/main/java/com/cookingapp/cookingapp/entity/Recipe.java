package com.cookingapp.cookingapp.entity;
import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.util.Util;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "recipe")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String imageUrl;

    @Column(length = 50)
    private String recipeName;

    @Column(length = 10)
    private String cookingTime;

    @Column(length = 10)
    private String preparationTime;

    private int servesFor;

    @Enumerated
    private DifficultyLevel difficultyLevel;

    @Enumerated
    private Category category;

    @Lob
    private String ingredients;

    @Lob
    private String instructions;

    @Column(columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    private int score;
    @PrePersist
    public void prePersist() {
        this.createDate = LocalDateTime.now();
    }

    public RecipeDto toDto(){
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(id);
        recipeDto.setImageUrl(imageUrl);
        recipeDto.setRecipeName(recipeName);
        recipeDto.setCookingTime(cookingTime);
        recipeDto.setPreparationTime(preparationTime);
        recipeDto.setTotalTime(Util.getTotalTime(cookingTime, preparationTime));
        recipeDto.setServesFor(servesFor);
        recipeDto.setDifficultyLevel(DifficultyLevel.toString(difficultyLevel));
        recipeDto.setCategory(Category.toString(category));
        recipeDto.setIngredients(this.convertIngredients());
        recipeDto.setInstructions(this.convertInstructions());
        recipeDto.setScore(score);
        return recipeDto;
    }

    private HashMap<Integer, ArrayList<String>> convertIngredients(){
        HashMap<Integer, ArrayList<String>> ingredientsMap = new HashMap<>();
        String[] splittedByComma = ingredients.split(" ,");

        int ingredientIndex = 0;
        for (String ingredient : splittedByComma) {
            ArrayList<String> ingredients = new ArrayList<>();
            Pattern pattern = Pattern.compile("(.*?\\s(?:bardağı|adet|kaşığı|gram|paket|kase|diş|litre))\\s*(.*)");
            Matcher matcher = pattern.matcher(ingredient);

            if (matcher.find()) {
                ingredients.add(Util.removeExtraSpaces(removeComma(matcher.group(1))));
                ingredients.add(Util.removeExtraSpaces(removeComma(matcher.group(2))));
            }
            ingredientsMap.put(ingredientIndex, ingredients);
            ingredientIndex++;
        }

        return ingredientsMap;
    }
    private HashMap<Integer, String> convertInstructions(){
        HashMap<Integer, String> instructionMap = new HashMap<>();
        String[] splittedByDot = instructions.split("\\.");

        int instructionIndex = 0;
        for (String instruction : splittedByDot) {
            if(!instruction.equals(" ")) {
                if(instruction.contains("<p>")) {
                    instruction = removeTags(instruction);
                }
                instructionMap.put(instructionIndex, Util.removeExtraSpaces(instruction));
                instructionIndex += 1;
            }

        }
        return instructionMap;
    }

    private String removeTags(String instruction) {
        if (instruction == null || instruction.isEmpty()) {
            return instruction;
        }

        String htmlTagPattern = "<.*?>";
        Pattern pattern = Pattern.compile(htmlTagPattern);
        Matcher matcher = pattern.matcher(instruction);
        return matcher.replaceAll("");
    }

    private static String removeComma(String ingredient) {
        if (ingredient == null || ingredient.isEmpty()) {
            return ingredient;
        }

        String htmlTagPattern = ",";
        Pattern pattern = Pattern.compile(htmlTagPattern);
        Matcher matcher = pattern.matcher(ingredient);
        return matcher.replaceAll("");
    }

}