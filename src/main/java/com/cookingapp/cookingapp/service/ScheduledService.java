package com.cookingapp.cookingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledService {

  private final RecipeService recipeService;

  @Scheduled(cron = "0 0 0 * * *")
  public void chooseDailyRandomRecipe() {
    this.recipeService.chooseDailyRandomRecipe();
  }

}
