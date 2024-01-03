package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.entity.Member;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.entity.RecipeMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeMemberRepository extends JpaRepository<RecipeMember, Long> {

  RecipeMember save(RecipeMember recipeMember);

  void deleteRecipeMemberByRecipe(Recipe recipe);

  @Query("SELECT rm.recipe FROM RecipeMember rm WHERE rm.member = :member")
  List<Recipe> getRecipesByMember(Member member);

  @Query("SELECT rm.recipe FROM RecipeMember rm WHERE rm.member.id = :memberId")
  List<Recipe> getRecipesByMemberId(Long memberId);
}
