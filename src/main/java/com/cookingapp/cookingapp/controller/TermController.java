package com.cookingapp.cookingapp.controller;


import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.service.RecipeService;
import com.cookingapp.cookingapp.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/terms")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    private final RecipeService recipeService;
    @GetMapping
    public ResponseEntity getTermByRecipe(@RequestParam Long id){
        RecipeDto recipeDto = recipeService.getRecipeById(id).toDto();
        return (ResponseEntity) ResponseEntity.ok();
    }

}
