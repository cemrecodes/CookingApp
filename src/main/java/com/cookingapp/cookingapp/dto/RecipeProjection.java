package com.cookingapp.cookingapp.dto;

import com.cookingapp.cookingapp.entity.Recipe;
import lombok.Data;

@Data
public class RecipeProjection {

    private Recipe recipe;
    private Boolean liked;
    private Boolean saved;

}
