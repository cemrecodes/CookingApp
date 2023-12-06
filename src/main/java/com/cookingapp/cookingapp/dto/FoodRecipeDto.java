package com.cookingapp.cookingapp.dto;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class FoodRecipeDto {

    private Long id;
    private String imageUrl;
    private String recipeName;
    private String cookingTime;
    private String preparationTime;
    private int servesFor;
    private String difficultyLevel;
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

}
