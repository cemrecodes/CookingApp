package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.util.JsonStructure;
import com.cookingapp.cookingapp.util.JsonStructure.GeminiRequest;
import com.cookingapp.cookingapp.util.JsonStructure.GeminiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/v1beta/models/")
public interface GeminiInterface {
  @GetExchange
  JsonStructure.ModelList getModels();

  @PostExchange("{model}:generateContent")
  GeminiResponse getCompletion(
      @PathVariable String model,
      @RequestBody GeminiRequest request);
}
