package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.SavedRecipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long> {

  SavedRecipe save(SavedRecipe recipeToSave);

  SavedRecipe findSavedRecipeByRecipeAndMember(Recipe recipe, Member member);

  SavedRecipe getSavedRecipeByRecipe_IdAndMember_Id(Long recipeId, Long memberId);
  void deleteSavedRecipeById(SavedRecipe savedRecipe);

  // List<Recipe> getSavedRecipesByMember(Member member);
  @Query("SELECT sr.recipe FROM SavedRecipe sr WHERE sr.member = :member")
  List<Recipe> getSavedRecipesByMember(Member member);


}
