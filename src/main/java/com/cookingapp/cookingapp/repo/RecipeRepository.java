package com.cookingapp.cookingapp.repo;

import com.cookingapp.cookingapp.dto.RecipeWithLikesAndSaves;
import com.cookingapp.cookingapp.entity.Category;
import com.cookingapp.cookingapp.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Recipe save(Recipe recipe);
    Recipe getRecipeById(Long id);
    List<Recipe> findAll();

    List<Recipe> findByRecipeName(String name);

    List<Recipe> findByCategory(Category category);

    void deleteRecipeById(Long id);

    @Query("SELECT "
        + "    r as recipe, "
        + "    CASE"
        + "        WHEN like.member.id IS NOT NULL THEN true "
        + "        ELSE false"
        + "    END AS liked,"
        + "    CASE "
        + "        WHEN s.member.id IS NOT NULL THEN true "
        + "        ELSE false "
        + "    END AS saved "
        + "FROM "
        + "    Recipe r "
        + "LEFT JOIN "
        + "    Like like ON r.id = like.recipe.id AND like.member.id = :memberId "
        + "LEFT JOIN "
        + "    SavedRecipe s ON r.id = s.recipe.id AND s.member.id = :memberId ")
    List<IRecipeResponse> getRecipesLoggedIn(Long memberId);


    @Query("SELECT new com.cookingapp.cookingapp.dto.RecipeWithLikesAndSaves(r, "
        + "CASE WHEN like.member.id IS NOT NULL THEN true ELSE false END, "
        + "CASE WHEN s.member.id IS NOT NULL THEN true ELSE false END ) "
        + "FROM "
        + "    Recipe r "
        + "LEFT JOIN "
        + "    Like like ON r.id = like.recipe.id AND like.member.id = :memberId "
        + "LEFT JOIN "
        + "    SavedRecipe s ON r.id = s.recipe.id AND s.member.id = :memberId ")
    List<RecipeWithLikesAndSaves> getRecipesLoggedIn2(Long memberId);


}
