package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.service.LikeService;
import com.cookingapp.cookingapp.service.MemberService;
import com.cookingapp.cookingapp.service.RecipeService;
import com.cookingapp.cookingapp.service.impl.ScrapeServiceImp;
import com.cookingapp.cookingapp.util.Util;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ScrapeServiceImp.class);

    private final ScrapeServiceImp scrapeServiceImp;

    private final RecipeService recipeService;

    private final MemberService memberService;

    private final LikeService likeService;

    @GetMapping(value = "/search")
    public ResponseEntity scrapeAndCreateNewFoodRecipe(@RequestParam(required = true) String foodName){
        logger.info("/v1/recipes/search endpoint has been called with @RequestParam = {}" , foodName);
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
        logger.info("/v1/recipes endpoint has been called");
        List<Recipe> recipeList = this.recipeService.getAllRecipe();
        return ResponseEntity.ok(recipeList.stream().map(Recipe::toDto));
    }

    @GetMapping(value="/category/{category}")
    public ResponseEntity getCategory(@PathVariable String category) {
        logger.info("/v1/recipes/category endpoint has been called with @PathVariable = {}" , category);
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
        logger.info("/v1/recipes/{id} endpoint has been called with @PathVariable = {}" , id);
        Recipe recipe = recipeService.getRecipeById(id);
        if( recipe != null ){
            return ResponseEntity.ok(recipeService.getRecipeById(id).toDto());
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteRecipe(@PathVariable Long id){
        logger.info("DELETE /v1/recipes/{id} endpoint has been called with @PathVariable = {}" , id);
        if (this.recipeService.getRecipeById(id) == null){
            return ResponseEntity.notFound().build();
        }
        else{
            this.recipeService.deleteRecipeById(id);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value = "/recipe-of-the-day")
    public ResponseEntity getRecipeOfTheDay(){
        logger.info("getRecipeOfTheDay has been called");
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
        logger.info("POST [LIKE] /v1/recipes/{recipeId}/like endpoint has been called with @PathVariable = {}" , recipeId);
        //String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal() ??
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            Optional<Member> member = memberService.getMemberByEmail(email);
            if(member.isPresent()){
                if(likeService.findLikeByRecipeAndMember(recipeId,member.get()) != null){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recipe has already been liked.");
                }
            }
            member.ifPresent(value -> likeService.save(recipeId, value));
        }
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{recipeId}/like")
    public ResponseEntity unlikeRecipe(@PathVariable Long recipeId) {
        logger.info("DELETE [UNLIKE] /v1/recipes/{recipeId}/like endpoint has been called with @PathVariable = {}" , recipeId);
        //String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal() ??
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            Optional<Member> member = memberService.getMemberByEmail(email);
            member.ifPresent(value -> likeService.delete(recipeId, value));
        }
        return ResponseEntity.ok().build();
    }



}
