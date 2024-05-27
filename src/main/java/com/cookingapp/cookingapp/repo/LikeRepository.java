package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.Like;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  Like save(Like like);
  Like findByRecipeAndMember(Recipe recipe, Member member);
  void deleteLikeById(Like like);
  @Query("SELECT like.recipe FROM Like like WHERE like.member = :member")
  List<Recipe> getLikedRecipesByMember(Member member);

  @Query("SELECT like.recipe.id FROM Like like WHERE like.member = :member")
  List<Long> getLikedRecipeIdsByMember(Member member);
}
