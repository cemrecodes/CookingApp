package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledServiceImp {

  private final RecipeService recipeService;

  @Scheduled(cron = "0 0 0 * * *")
  public void chooseDailyRandomRecipe() {
    this.recipeService.chooseDailyRandomRecipe();
  }

}
