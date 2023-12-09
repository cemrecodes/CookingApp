package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.service.RecipeService;
import com.cookingapp.cookingapp.service.impl.ScrapeServiceImp;
import com.cookingapp.cookingapp.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final ScrapeServiceImp scrapeServiceImp;

    private final RecipeService recipeService;

    @GetMapping(value = "/search")
    public ResponseEntity scrapeAndCreateNewFoodRecipe(@RequestParam(required = true) String foodName){
        ResponseEntity result;
        foodName = Util.removeSpaces(foodName);
        List<Recipe> foundRecipe = recipeService.getRecipeByName(Util.splitByDashAndUpperCaseInitials(foodName));

        if (!foundRecipe.isEmpty()) {
            return ResponseEntity.ok(foundRecipe.get(0).toDto());
        }

        foodName = Util.turkishCharsToEnglish(foodName);
        RecipeDto recipe = this.scrapeServiceImp.scrapeAndCreateNewRecipe(foodName);
        if (recipe != null) {
            result = ResponseEntity.ok(recipe);
        } else {
            result = ResponseEntity.badRequest().body("There was an error with scraping.");
        }
        return result;
    }

    @GetMapping
    public ResponseEntity getAllRecipes(){
        // TODO ENTITY TO DTO
        /*
        List<RecipeDto> recipeList = this.recipeService.getAllRecipe()
                .stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .collect(Collectors.toList());

         */
        List<Recipe> recipeList = this.recipeService.getAllRecipe();
        return ResponseEntity.ok(recipeList.stream().map(Recipe::toDto));
    }

    @GetMapping(value="/category/{category}")
    public ResponseEntity getCategory(@PathVariable String category) {
        return ResponseEntity.ok(recipeService.getRecipesByCategory(category).stream().map(Recipe::toDto));
    }
}
