package com.cookingapp.cookingapp.service.impl;


import com.cookingapp.cookingapp.entity.RecipeDraft;
import com.cookingapp.cookingapp.entity.RecipeStatus;
import com.cookingapp.cookingapp.repo.RecipeDraftRepository;
import com.cookingapp.cookingapp.service.RecipeDraftService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeDraftServiceImp implements RecipeDraftService {

  private final RecipeDraftRepository draftRepository;

  @Override
  public RecipeDraft save(RecipeDraft recipeDraft) {
    return draftRepository.save(recipeDraft);
  }

  @Override
  public RecipeDraft getRecipeDraftById(Long id) {
    return draftRepository.getRecipeDraftById(id);
  }

  @Override
  public List<RecipeDraft> getRecipeDraftsByMemberId(Long memberId) {
    return draftRepository.getRecipeDraftsByMemberId(memberId);
  }

  @Override
  public List<RecipeDraft> getRecipeDraftsByStatus(RecipeStatus status) {
    return draftRepository.getRecipeDraftsByStatus(status);
  }
}
