package com.cookingapp.cookingapp.entity;

import com.cookingapp.cookingapp.dto.InstructionDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "recipe_draft")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class RecipeDraft {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Member memberId;

  @Lob
  private String image;

  @Column(length = 50)
  private String recipeName;

  @Column(length = 20)
  private String cookingTime;

  @Column(length = 20)
  private String preparationTime;

  @Column(length = 20)
  private String servesFor;

  @Enumerated
  private Category category;

  @OneToMany(mappedBy = "recipe_draft", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<IngredientDraft> ingredients;

  @Lob
  private String instructions;

  @Column(columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createDate;

  @Enumerated
  private RecipeStatus status;

  private String reason;

  @PrePersist
  public void prePersist() {
    this.createDate = LocalDateTime.now();
    if( status == null ){
      this.status = RecipeStatus.IN_PROGRESS;
    }
  }

  public String toStringForChatGpt(){
    return recipeName + " Pişirme Süresi: " + cookingTime + " Hazırlama Süresi: " + preparationTime +
        " " + servesFor + " kişilik Yapılışı: " + instructions;
  }

  public Recipe convertToRecipe(){
    Recipe recipe = new Recipe();
    recipe.setImage(image);
    recipe.setRecipeName(recipeName);
    recipe.setCookingTime(cookingTime);
    recipe.setPreparationTime(preparationTime);
    recipe.setServesFor(servesFor);
    recipe.setCategory(category);
    recipe.setInstructions(instructions);
    recipe.setTermsAdded(false);
    return recipe;
  }


}