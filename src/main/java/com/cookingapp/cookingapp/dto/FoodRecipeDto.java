package com.cookingapp.cookingapp.dto;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.ArrayList;

@Data
public class FoodRecipeDto {

    private Long id;
    private String recipeName;
    private String cookingTime;
    private String preparationTime;
    private int servesFor;
    private ArrayList<String> ingredients;
    private String method;
    private OffsetDateTime createTime;
    private int score;

}
