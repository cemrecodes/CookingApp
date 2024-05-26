package com.cookingapp.cookingapp.service;


import com.cookingapp.cookingapp.entity.Like;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.repo.LikeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    private final RecipeService recipeService;

    private final RecipeESService recipeESService;

    @Transactional
    public Like save(Long recipeId, Member member) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        recipe.setLikeCount(recipe.getLikeCount() + 1);
        Like like = new Like();
        like.setRecipe(recipe);
        like.setMember(member);
        recipeService.save(recipe);
        recipeESService.save(recipe.toRecipeES());
        return likeRepository.save(like);
    }

    @Transactional
    public void delete(Long recipeId, Member member) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        recipe.setLikeCount(recipe.getLikeCount() - 1);
        Like like = findLikeByRecipeAndMember(recipe, member);
        recipeService.save(recipe);
        recipeESService.save(recipe.toRecipeES());
        likeRepository.delete(like);
    }

    
    public Like findLikeByRecipeAndMember(Recipe recipe, Member member) {
        return likeRepository.findByRecipeAndMember(recipe, member);
    }

    
    public Like findLikeByRecipeAndMember(Long recipeId, Member member) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return likeRepository.findByRecipeAndMember(recipe, member);
    }

    
    public List<Recipe> getLikedRecipesByMember(Member member) {
        return likeRepository.getLikedRecipesByMember(member);
    }

}
