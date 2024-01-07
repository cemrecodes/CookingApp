package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.RecipeDraft;
import com.cookingapp.cookingapp.entity.RecipeStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeDraftRepository extends JpaRepository<RecipeDraft, Long> {
  RecipeDraft save(RecipeDraft recipeDraft);

  RecipeDraft getRecipeDraftById(Long id);

  List<RecipeDraft> getRecipeDraftsByMemberId(Long memberId);

  List<RecipeDraft> getRecipeDraftsByStatus(RecipeStatus status);

}
