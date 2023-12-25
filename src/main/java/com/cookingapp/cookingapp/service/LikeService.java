package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Like;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;

public interface LikeService {

  Like save(Long recipeId, Member member);

  void delete(Long recipeId, Member member);

  Like findLikeByRecipeAndMember(Recipe recipe, Member member);
}
