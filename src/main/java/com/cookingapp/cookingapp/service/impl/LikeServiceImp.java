package com.cookingapp.cookingapp.service.impl;


import com.cookingapp.cookingapp.entity.Like;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.repo.LikeRepository;
import com.cookingapp.cookingapp.service.LikeService;
import com.cookingapp.cookingapp.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImp implements LikeService {

    private final LikeRepository likeRepository;

    private final RecipeService recipeService;

    @Override
    public Like save(Long recipeId, Member member) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        Like like = new Like();
        like.setRecipe(recipe);
        like.setMember(member);
        return likeRepository.save(like);
    }

    @Override
    public void delete(Long recipeId, Member member) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        Like like = findLikeByRecipeAndMember(recipe, member);
        likeRepository.deleteLikeById(like);
    }

    @Override
    public Like findLikeByRecipeAndMember(Recipe recipe, Member member) {
        return likeRepository.findByRecipeAndMember(recipe, member);
    }

    @Override
    public Like findLikeByRecipeAndMember(Long recipeId, Member member) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return likeRepository.findByRecipeAndMember(recipe, member);
    }

}
