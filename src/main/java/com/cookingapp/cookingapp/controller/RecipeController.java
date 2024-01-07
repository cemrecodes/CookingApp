package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.dto.IngredientDto;
import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.IngredientDraft;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.RecipeDraft;
import com.cookingapp.cookingapp.model.RecipeES;
import com.cookingapp.cookingapp.service.AsyncRecipeService;
import com.cookingapp.cookingapp.service.LikeService;
import com.cookingapp.cookingapp.service.MemberService;
import com.cookingapp.cookingapp.service.RecipeESService;
import com.cookingapp.cookingapp.service.RecipeService;
import com.cookingapp.cookingapp.service.RecipeServiceFacade;
import com.cookingapp.cookingapp.service.SavedRecipeService;
import com.cookingapp.cookingapp.service.impl.ScrapeServiceImp;
import com.cookingapp.cookingapp.util.Util;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final ScrapeServiceImp scrapeServiceImp;

    private final RecipeService recipeService;

    private final MemberService memberService;

    private final LikeService likeService;

    private final SavedRecipeService savedRecipeService;

    private final RecipeESService recipeESService;

    private final RecipeServiceFacade recipeServiceFacade;

    private final AsyncRecipeService asyncRecipeService;

    @GetMapping(value = "/search")
    public ResponseEntity scrapeAndCreateNewFoodRecipe(@RequestParam(required = true) String foodName){
        log.info("/v1/recipes/search endpoint has been called with @RequestParam = {}" , foodName);
        ResponseEntity result;
        foodName = Util.removeSpaces(foodName);
        // List<Recipe> foundRecipe = recipeService.getRecipeByName(Util.splitByDashAndUpperCaseInitials(foodName));

        List<RecipeES> foundRecipeList = recipeESService.searchRecipe(foodName);

        if (!foundRecipeList.isEmpty()) {
           //  return ResponseEntity.ok(foundRecipe.get(0).toDto());
            return ResponseEntity.ok(foundRecipeList);
        }

        foodName = Util.turkishCharsToEnglish(foodName);
        Recipe recipe = this.scrapeServiceImp.scrapeAndCreateNewRecipe(foodName);
        if (recipe != null) {
            result = ResponseEntity.ok(recipe.toDto());
        } else {
            result = ResponseEntity.badRequest().body("There was an error with scraping.");
        }
        return result;
    }

    @GetMapping
    public ResponseEntity<List<RecipeDto>> getAllRecipes(){
        log.info("/v1/recipes endpoint has been called");
        List<Recipe> recipeList = this.recipeService.getAllRecipe();
        return ResponseEntity.ok(recipeList.stream().map(Recipe::toDto).toList());
    }

    @GetMapping(value="/category/{category}")
    public ResponseEntity<List<RecipeDto>> getCategory(@PathVariable String category) {
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
        return ResponseEntity.ok(recipeService.getRecipesByCategory(category).stream().map(Recipe::toDto).toList());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable Long id){
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
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id){
        log.info("DELETE /v1/recipes/{id} endpoint has been called with @PathVariable = {}" , id);
        if (this.recipeService.getRecipeById(id) == null){
            return ResponseEntity.notFound().build();
        }
        else{
            this.recipeService.deleteRecipeById(id);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value = "/recipe-of-the-day")
    public ResponseEntity<RecipeDto> getRecipeOfTheDay(){
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
    public ResponseEntity<String> likeRecipe(@PathVariable Long recipeId) {
        log.info("POST [LIKE] /v1/recipes/{recipeId}/like endpoint has been called with @PathVariable = {}" , recipeId);
        //String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal() ??
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            Optional<Member> member = memberService.getMemberByEmail(email);
            if(member.isPresent() && likeService.findLikeByRecipeAndMember(recipeId,member.get()) != null){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe has already been liked.");

            }
            member.ifPresent(value -> likeService.save(recipeId, value));
        }
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{recipeId}/like")
    public ResponseEntity<Void> unlikeRecipe(@PathVariable Long recipeId) {
        log.info("DELETE [UNLIKE] /v1/recipes/{recipeId}/like endpoint has been called with @PathVariable = {}" , recipeId);
        //String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal() ??
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            Optional<Member> member = memberService.getMemberByEmail(email);
            member.ifPresent(value -> likeService.delete(recipeId, value));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{recipeId}/save")
    public ResponseEntity<String> saveRecipe(@PathVariable Long recipeId) {
        log.info("POST [SAVE] /v1/recipes/{recipeId}/save endpoint has been called with @PathVariable = {}" , recipeId);
        //String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal() ??
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            Optional<Member> member = memberService.getMemberByEmail(email);
            if(member.isPresent() && savedRecipeService.findSaveByRecipeAndMember(recipeId, member.get()) != null){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe has already been saved.");
            }
            member.ifPresent(value -> savedRecipeService.save(recipeId, value));
        }
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{recipeId}/save")
    public ResponseEntity<Void> unsaveRecipe(@PathVariable Long recipeId) {
        log.info("DELETE [SAVE] /v1/recipes/{recipeId}/save endpoint has been called with @PathVariable = {}" , recipeId);
        //String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal() ??
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            Optional<Member> member = memberService.getMemberByEmail(email);
            member.ifPresent(value -> savedRecipeService.delete(recipeId, value));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/saved")
    public ResponseEntity<List<RecipeDto>> getSavedRecipes(){
        log.info("getSavedRecipes has been called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            Optional<Member> member = memberService.getMemberByEmail(email);
            if( member.isPresent() ){
                List<RecipeDto> savedRecipes = savedRecipeService.getSavedRecipesByMember(member.get())
                    .stream()
                    .map(Recipe::toDto)
                    .toList();
                return ResponseEntity.ok(savedRecipes);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/liked")
    public ResponseEntity<List<RecipeDto>> getLikedRecipes(){
        log.info("getLikedRecipes has been called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            Optional<Member> member = memberService.getMemberByEmail(email);
            if( member.isPresent() ){
                List<RecipeDto> likedRecipes = likeService.getLikedRecipesByMember(member.get())
                    .stream()
                    .map(Recipe::toDto)
                    .toList();
                return ResponseEntity.ok(likedRecipes);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/deleteAll")
    public ResponseEntity<Void> deleteAllElastic(){
        recipeESService.deleteAll();
        return ResponseEntity.ok().build();
    }
    @GetMapping(value = "/addToElastic")
    public ResponseEntity<List<RecipeES>> addRecipesToElastic(){
        List<Recipe> recipeList = recipeService.getAllRecipe();
        List<RecipeES> recipeESList = new ArrayList<>();
        for(Recipe recipe: recipeList){
            recipeESList.add(recipe.toRecipeES());
        }
        recipeESList = recipeESService.saveAll(recipeESList);
        return ResponseEntity.ok(recipeESList);
    }

    // todo foto ekleme??
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RecipeDraft> addRecipe(@RequestBody RecipeDto recipeDto){
        log.info("addRecipe has been called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            Optional<Member> member = memberService.getMemberByEmail(email);
            if( member.isPresent() ){
                List<IngredientDraft> ingredientList = recipeDto.getIngredients().stream()
                    .map(IngredientDto::toIngredientDraft)
                    .toList();
                RecipeDraft recipeDraft = recipeServiceFacade.saveRecipeToDraft(recipeDto.convertToRecipeDraft(), ingredientList, member.get());
                asyncRecipeService.processRecipeDraft(recipeDraft);
                /*
                List<Ingredient> ingredientList = recipeDto.getIngredients().stream()
                    .map(IngredientDto::toIngredient)
                    .toList();
                Recipe recipe = recipeServiceFacade.saveRecipe(recipeDto.convertToRecipe(), ingredientList, member.get());

                 */
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
