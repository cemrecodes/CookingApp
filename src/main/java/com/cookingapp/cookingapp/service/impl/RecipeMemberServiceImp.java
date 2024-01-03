package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.RecipeMember;
import com.cookingapp.cookingapp.repo.RecipeMemberRepository;
import com.cookingapp.cookingapp.service.RecipeMemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeMemberServiceImp implements RecipeMemberService {

  private final RecipeMemberRepository recipeMemberRepository;

  @Override
  public RecipeMember save(Recipe recipe, Member member) {
    RecipeMember recipeMember = new RecipeMember();
    recipeMember.setRecipe(recipe);
    return recipeMemberRepository.save(recipeMember);
  }

  @Override
  public void delete(Recipe recipe) {
    recipeMemberRepository.deleteRecipeMemberByRecipe(recipe);
  }

  @Override
  public List<Recipe> getRecipesByMember(Member member) {
    return recipeMemberRepository.getRecipesByMember(member);
  }

  @Override
  public List<Recipe> getRecipesByMemberId(Long memberId) {
    return recipeMemberRepository.getRecipesByMemberId(memberId);
  }
}
