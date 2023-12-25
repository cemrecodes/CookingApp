package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.Like;
import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  Like save(Like like);
  Like findByRecipeAndMember(Recipe recipe, Member member);
  void deleteLikeById(Like like);

}
