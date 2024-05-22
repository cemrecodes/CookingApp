package com.cookingapp.cookingapp.service;


import com.cookingapp.cookingapp.entity.Like;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.repo.LikeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    private final RecipeService recipeService;

    
    public Like save(Long recipeId, Member member) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        Like like = new Like();
        like.setRecipe(recipe);
        like.setMember(member);
        return likeRepository.save(like);
    }

    
    public void delete(Long recipeId, Member member) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        Like like = findLikeByRecipeAndMember(recipe, member);
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
