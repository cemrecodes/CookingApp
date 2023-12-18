package com.cookingapp.cookingapp.entity;
import com.cookingapp.cookingapp.dto.IngredientDto;
import com.cookingapp.cookingapp.dto.InstructionDto;
import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.util.Util;
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
import jakarta.persistence.criteria.CriteriaBuilder.In;
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

    @Column(length = 20)
    private String cookingTime;

    @Column(length = 20)
    private String preparationTime;

    @Column(length = 20)
    private String servesFor;

    @Enumerated
    private DifficultyLevel difficultyLevel;

    @Enumerated
    private Category category;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ingredient> ingredients;

    @Lob
    private String instructions;

    @Column(columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    private int score;

    private boolean termsAdded;
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

    private ArrayList<IngredientDto> convertIngredients(){
        ArrayList<IngredientDto> ingredientList = new ArrayList<>();

        for (Ingredient ingredient: ingredients) {
            IngredientDto ingredientDto = new IngredientDto();
            ingredientDto.setIngredient(ingredient.getIngredient());
            ingredientDto.setAmount(ingredient.getAmount());
            ingredientList.add(ingredientDto);
        }

        return ingredientList;
    }
    /*
    private HashMap<Integer, ArrayList<String>> convertIngredients(){
        HashMap<Integer, ArrayList<String>> ingredientsMap = new HashMap<>();

        int ingredientIndex = 0;
        for (Ingredient ingredient: ingredients) {
            ArrayList<String> ingredients = new ArrayList<>();
            ingredients.add(ingredient.getIngredient());
            ingredients.add(ingredient.getAmount());
            ingredientsMap.put(ingredientIndex, ingredients);
            ingredientIndex++;
        }

        return ingredientsMap;
    }

    /*
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

     */
    private ArrayList<InstructionDto> convertInstructions(){
        ArrayList<InstructionDto> instructionArray = new ArrayList<>();
        String[] splittedByDot = instructions.split("\\.");

        for (String instruction : splittedByDot) {
            InstructionDto instructionDto = new InstructionDto();
            if(!instruction.equals(" ")) {
                instruction = Util.removeTags(instruction);
                instructionDto.setInstruction(instruction);
                instructionDto.setTime(Util.findPrepOrCookTime(instruction));
                instructionArray.add(instructionDto);
            }
        }

        return instructionArray;
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