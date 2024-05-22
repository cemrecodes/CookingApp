package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.Score;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

  @Query("SELECT COUNT(s) FROM Score s WHERE s.recipe.id = :recipeId")
  int countScoresByRecipeId(@Param("recipeId") Long recipeId);

  Score save(Score score);

  Optional<Score> getByRecipeAndMember(Recipe recipe, Member member);
}
