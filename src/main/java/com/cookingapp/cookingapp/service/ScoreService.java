package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.Score;
import com.cookingapp.cookingapp.repo.ScoreRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreService {

  private final ScoreRepository scoreRepository;

  void save(Score score){
    scoreRepository.save(score);
  }
  void saveScore(Recipe recipe, Member member, Double score) {
    Score scoreObject = new Score();
    scoreObject.setRecipe(recipe);
    scoreObject.setMember(member);
    scoreObject.setScore(score);
    scoreRepository.save(scoreObject);
  }

  int countScoresByRecipeId(Long recipeId){
    return this.scoreRepository.countScoresByRecipeId(recipeId);
  }

  Optional<Score> isRecipeRatedByMember(Recipe recipe, Member member){
    return this.scoreRepository.getByRecipeAndMember(recipe, member);
  }
}
