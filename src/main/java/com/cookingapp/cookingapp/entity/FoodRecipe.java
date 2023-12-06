package com.cookingapp.cookingapp.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.OffsetDateTime;

@Entity
@Table(name = "food_recipe")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class FoodRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String recipeName;

    @Column(length = 10)
    private String cookingTime;

    @Column(length = 10)
    private String preparationTime;

    private int servesFor;

    @Enumerated
    private DifficultyLevel difficultyLevel;

    @Lob
    private String ingredients;

    @Lob
    private String method;

    @Column
    private OffsetDateTime createTime;

    private int score;
    @PrePersist
    public void prePersist() {
        this.createTime = OffsetDateTime.now();
    }
}