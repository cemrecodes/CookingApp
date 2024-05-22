package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.RecipeMember;
import com.cookingapp.cookingapp.repo.RecipeMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeMemberService {

  private final RecipeMemberRepository recipeMemberRepository;

  
  public RecipeMember save(Recipe recipe, Member member) {
    RecipeMember recipeMember = new RecipeMember();
    recipeMember.setRecipe(recipe);
    recipeMember.setMember(member);
    return recipeMemberRepository.save(recipeMember);
  }

  
  public void delete(Recipe recipe) {
    recipeMemberRepository.deleteRecipeMemberByRecipe(recipe);
  }

  
  public List<Recipe> getRecipesByMember(Member member) {
    return recipeMemberRepository.getRecipesByMember(member);
  }

  
  public List<Recipe> getRecipesByMemberId(Long memberId) {
    return recipeMemberRepository.getRecipesByMemberId(memberId);
  }
}
