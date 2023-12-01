package com.cookingapp.cookingapp.dto;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@Data
public class FoodRecipeDto {

    private Long id;
    private String recipeName;
    private String cookingTime;
    private String preparationTime;
    private int servesFor;
    private HashMap<Integer, ArrayList<String>> ingredients;
    private HashMap<Integer, String> instructions;
    private OffsetDateTime createTime;
    private int score;

}
