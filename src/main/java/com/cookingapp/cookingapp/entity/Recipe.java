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
import java.lang.reflect.Array;
import java.util.stream.Collectors;
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
        List<IngredientDto> ingredientDtos = ingredients.stream()
            .map(Ingredient::toDto)
            .toList();
        recipeDto.setIngredients(new ArrayList<>(ingredientDtos));
        recipeDto.setInstructions(this.convertInstructions());
        recipeDto.setScore(score);
        return recipeDto;
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

}