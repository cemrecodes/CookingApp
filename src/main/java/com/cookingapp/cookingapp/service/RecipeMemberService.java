package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.RecipeMember;
import java.util.List;

public interface RecipeMemberService {

  RecipeMember save(Recipe recipe, Member member);

  void delete(Recipe recipe);

  List<Recipe> getRecipesByMember(Member member);

  List<Recipe> getRecipesByMemberId(Long memberId);

}
