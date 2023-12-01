package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.dto.FoodRecipeDto;
import com.cookingapp.cookingapp.service.ScrapeService;
import com.cookingapp.cookingapp.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/v1/foodRecipe")
@RequiredArgsConstructor
public class FoodRecipeController {

    private final ScrapeService scrapeService;
    @PostMapping(value = "/search")
    public ResponseEntity scrapeAndCreateNewFoodRecipe(@RequestParam(required = true) String foodName){
        ResponseEntity result;
        foodName = Util.turkishCharsToEnglish(foodName);
        FoodRecipeDto recipe = this.scrapeService.scrapeAndCreateNewFoodRecipe(foodName);
        if (recipe != null) {
            result = ResponseEntity.ok(recipe);
        } else {
            result = ResponseEntity.badRequest().body("There was an error with scraping.");
        }
        return result;
    }

}
