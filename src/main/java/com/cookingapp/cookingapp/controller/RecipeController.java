package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.dto.HeaderResponseWithDetail;
import com.cookingapp.cookingapp.dto.IngredientDto;
import com.cookingapp.cookingapp.dto.InstructionExplanation;
import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.dto.RecipeWithLikesAndSaves;
import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.model.RecipeES;
import com.cookingapp.cookingapp.response.RecipeHeaderResponse;
import com.cookingapp.cookingapp.service.AuthenticationService;
import com.cookingapp.cookingapp.service.LikeService;
import com.cookingapp.cookingapp.service.MemberService;
import com.cookingapp.cookingapp.service.RecipeESService;
import com.cookingapp.cookingapp.service.RecipeService;
import com.cookingapp.cookingapp.service.RecipeServiceFacade;
import com.cookingapp.cookingapp.service.SavedRecipeService;
import com.cookingapp.cookingapp.service.ScrapeService;
import com.cookingapp.cookingapp.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/recipes")
@Slf4j
@RequiredArgsConstructor
public class RecipeController {

    private final ScrapeService scrapeService;

    private final RecipeService recipeService;

    private final LikeService likeService;

    private final SavedRecipeService savedRecipeService;

    private final RecipeESService recipeESService;

    private final RecipeServiceFacade recipeServiceFacade;

    private final AuthenticationService authenticationService;

    @GetMapping(value = "/search")
    public ResponseEntity<List<RecipeHeaderResponse>> scrapeAndCreateNewFoodRecipe(@RequestParam(required = false) String category, @RequestParam(required = true) String foodName){
        log.info("/v1/recipes/search endpoint has been called with @RequestParam foodName = {} , category = {}" , foodName, category);

        switch (category){
            case "corba": {
                category = "çorba";
                break;
            }
            case "ana-yemek": {
                category = "ana yemek";
                break;
            }
            case "tatli": {
                category = "tatlı";
                break;
            }
            case "icecek": {
                category = "içecek";
                break;
            }
            default:
                category = null;
        }

        List<RecipeHeaderResponse> foundRecipe = new ArrayList<>(
            recipeESService.searchRecipe(foodName).stream().map(RecipeES::toHeaderResponse).toList());

        if ( !foundRecipe.isEmpty() ) {
            if ( category != null ) {
                String finalCategory = category;
                foundRecipe.removeIf(recipe -> !recipe.getCategory().equals(finalCategory));
            }
            // todo scrapeledi ama uygun değil / bulamadı -> not found
          return ResponseEntity.ok(foundRecipe);
        }

        foodName = Util.removeSpaces(foodName);
        foodName = Util.turkishCharsToEnglish(foodName);
        Recipe recipe = this.scrapeService.scrapeAndCreateNewRecipe(foodName);
        if (recipe != null){
            if( category != null && !recipe.getCategory().toString().equals(category)) {
                return ResponseEntity.ok().build();
            }
            else{
                return ResponseEntity.ok(Collections.singletonList(recipe.toHeaderResponse()));
            }
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity getAllRecipes(){
        log.info("/v1/recipes endpoint has been called");
        List<Recipe> recipeList = this.recipeService.getAllRecipe();
        return ResponseEntity.ok(recipeList.stream().map(Recipe::toDto));
    }


    @GetMapping("/test/projection")
    public ResponseEntity getRecipeProjection(){
        return ResponseEntity.ok(this.recipeService.getRecipeProjection());
    }

    @GetMapping("/testByMember/{memberId}")
    public ResponseEntity getAllRecipesByMemberId(@PathVariable Long memberId){
        log.info("/v1/recipes endpoint has been called");
        List<RecipeWithLikesAndSaves> recipeWithLikesAndSavesList = this.recipeService.getAllRecipeLoggedIn(memberId);

        // todo rating
        List<HeaderResponseWithDetail> responseList = recipeWithLikesAndSavesList.stream()
            .map(r -> new HeaderResponseWithDetail(r.getRecipe().toDto(), r.isLiked(), r.isSaved()))
            .toList();

        return ResponseEntity.ok(responseList);
    }

    // todo elastic veri (foto) güncelle
    @GetMapping(value = "/test/es")
    public ResponseEntity getAllRecipesES(){
        log.info("/v1/recipes/es endpoint has been called");
        List<RecipeES> recipeList = this.recipeESService.getAll();
        return ResponseEntity.ok(recipeList);
    }

    @GetMapping(value="/category/{category}")
    public ResponseEntity getCategory(@PathVariable String category) {
        log.info("/v1/recipes/category endpoint has been called with @PathVariable = {}" , category);
        switch (category){
            case "corba": {
                category = "çorba";
                break;
            }
            case "ana-yemek": {
                category = "ana yemek";
                break;
            }
            case "tatli": {
                category = "tatlı";
                break;
            }
            case "icecek": {
                category = "içecek";
                break;
            }
            default:
                category = null;
        }
        return ResponseEntity.ok(recipeService.getRecipesByCategory(category).stream().map(Recipe::toDto));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getRecipe(@PathVariable Long id){
        log.info("/v1/recipes/{id} endpoint has been called with @PathVariable = {}" , id);
        Recipe recipe = recipeService.getRecipeById(id);
        if( recipe != null ){
            return ResponseEntity.ok(recipeService.getRecipeById(id).toDto());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteRecipe(@PathVariable Long id){
        log.info("DELETE /v1/recipes/{id} endpoint has been called with @PathVariable = {}" , id);
        if (this.recipeService.getRecipeById(id) == null){
            return ResponseEntity.notFound().build();
        }
        else{
            this.recipeService.deleteRecipeById(id);
            this.recipeESService.delete(id);
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping(value = "/test/es/{id}")
    public ResponseEntity deleteRecipeES(@PathVariable Long id){
        log.info("DELETE /v1/recipes/es/{id} endpoint has been called with @PathVariable = {}" , id);
        this.recipeESService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/recipe-of-the-day")
    public ResponseEntity getRecipeOfTheDay(){
        log.info("getRecipeOfTheDay has been called");
        if(this.recipeService.getDailyRandomRecipe() != null){
            return ResponseEntity.ok(this.recipeService.getDailyRandomRecipe().toDto());
        }
        else{
            this.recipeService.chooseDailyRandomRecipe();
            return ResponseEntity.ok(this.recipeService.getDailyRandomRecipe().toDto());
        }
    }

    @PostMapping("/{recipeId}/like")
    public ResponseEntity likeRecipe(@PathVariable Long recipeId) {
        log.info("POST [LIKE] /v1/recipes/{recipeId}/like endpoint has been called with @PathVariable = {}" , recipeId);
        Member member = authenticationService.isAuthenticated();
        if(member != null){
            if(likeService.findLikeByRecipeAndMember(recipeId,member) != null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe has already been liked.");
            }
            else{
                likeService.save(recipeId, member);
            }
        }
        return ResponseEntity.ok().body(recipeService.getRecipeById(recipeId));
    }
    @DeleteMapping("/{recipeId}/like")
    public ResponseEntity unlikeRecipe(@PathVariable Long recipeId) {
        log.info("DELETE [UNLIKE] /v1/recipes/{recipeId}/like endpoint has been called with @PathVariable = {}" , recipeId);
        Member member = authenticationService.isAuthenticated();
        if (member != null) {
            likeService.delete(recipeId, member);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{recipeId}/save")
    public ResponseEntity saveRecipe(@PathVariable Long recipeId) {
        log.info("POST [SAVE] /v1/recipes/{recipeId}/save endpoint has been called with @PathVariable = {}" , recipeId);
        // todo bu ne? token tipleri? isAuthenticated? Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = authenticationService.isAuthenticated();
        if (member != null) {
            if(savedRecipeService.findSaveByRecipeAndMember(recipeId, member) != null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe has already been saved.");
            }
            else {
                savedRecipeService.save(recipeId, member);
            }
        }
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{recipeId}/save")
    public ResponseEntity unsaveRecipe(@PathVariable Long recipeId) {
        log.info("DELETE [SAVE] /v1/recipes/{recipeId}/save endpoint has been called with @PathVariable = {}" , recipeId);
        Member member = authenticationService.isAuthenticated();
        if (member != null) {
            savedRecipeService.delete(recipeId, member);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/saved")
    public ResponseEntity getSavedRecipes(){
        log.info("getSavedRecipes has been called");
        Member member = authenticationService.isAuthenticated();
        if (member != null) {
                List<RecipeDto> savedRecipes = savedRecipeService.getSavedRecipesByMember(member)
                    .stream()
                    .map(Recipe::toDto)
                    .toList();
                return ResponseEntity.ok(savedRecipes);
            }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/liked")
    public ResponseEntity getLikedRecipes(){
        log.info("getLikedRecipes has been called");
        Member member = authenticationService.isAuthenticated();
        if (member != null) {
                List<RecipeDto> likedRecipes = likeService.getLikedRecipesByMember(member)
                    .stream()
                    .map(Recipe::toDto)
                    .toList();
                return ResponseEntity.ok(likedRecipes);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/test/deleteAll")
    public ResponseEntity deleteAllElastic(){
        recipeESService.deleteAll();
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/test/addToElastic")
    public ResponseEntity<List<RecipeES>> addRecipesToElastic(){
        List<Recipe> recipeList = recipeService.getAllRecipe();
        List<RecipeES> recipeESList = new ArrayList<>();
        for(Recipe recipe: recipeList){
            recipeESList.add(recipe.toRecipeES());
        }
        recipeESList = recipeESService.saveAll(recipeESList);
        return ResponseEntity.ok(recipeESList);
    }

    // todo foto ekleme dene
    @PostMapping
    // @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RecipeDto> addRecipe(@RequestBody RecipeDto recipeDto){
        log.info("addRecipe has been called");
        Member member = authenticationService.isAuthenticated();
        if (member != null) {
                List<Ingredient> ingredientList = recipeDto.getIngredients().stream()
                    .map(IngredientDto::toIngredient)
                    .toList();
                Recipe recipe = recipeServiceFacade.saveRecipe(recipeDto.convertToRecipe(), ingredientList, member);
                return ResponseEntity.ok(recipe.toDto());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("/{recipeId}/instructions/{instructionIndex}/details")
    public ResponseEntity explainInstruction(@PathVariable Long recipeId, @RequestParam Long instructionIndex) {
        log.info("explainInstruction has been called");
        // todo sadece giriş yapanlara özel mi olmalı
        /*
        Member member = authenticationService.isAuthenticated();
        if (member != null) { */
            String explanation = recipeService.explainInstruction(recipeId, instructionIndex);
            InstructionExplanation instructionExplanation = new InstructionExplanation();
            instructionExplanation.setExplanation(explanation);
            return ResponseEntity.ok(instructionExplanation);
        /* }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); */
    }

    @GetMapping("/instructionDetail")
    public ResponseEntity<InstructionExplanation> explain(@RequestParam String instruction) {
        log.info("explainInstruction has been called");
        String explanation = recipeService.explainInstruction(instruction);
        InstructionExplanation instructionExplanation = new InstructionExplanation();
        instructionExplanation.setExplanation(explanation);
        return ResponseEntity.ok(instructionExplanation);
    }

}
