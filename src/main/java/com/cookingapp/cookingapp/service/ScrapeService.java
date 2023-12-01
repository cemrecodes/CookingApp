package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.FoodRecipeDto;
import com.cookingapp.cookingapp.util.Util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScrapeService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ScrapeService.class);

    public FoodRecipeDto scrapeAndCreateNewFoodRecipe(String foodName){
        String permaLink = this.searchFoodRecipeUrl(foodName);
        Result result = this.scrapeUrl("https://www.yemek.com", permaLink);
        FoodRecipeDto recipe = this.getFoodRecipe(result.getScript(), result.getDocument());
        return recipe;
    }

    public String searchFoodRecipeUrl(String foodName){
        StringBuilder fullUrl = new StringBuilder("https://zagorapi.yemek.com/search/recipe?query=");
        fullUrl.append(foodName);
        fullUrl.append("&start=0&limit=1");
        String firstPermalink = null;

        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl.toString()))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            firstPermalink = jsonNode.path("Data").path("Posts").get(0).path("Permalink").asText();
        } catch (Exception e){
            logger.error("Couldn't get response: {}", e.getMessage());
        }

        return firstPermalink;
    }

    public Result scrapeUrl(String url, String foodName){
        String permaLink = this.searchFoodRecipeUrl(foodName);
        StringBuilder fullUrl = new StringBuilder(url);
        fullUrl.append(permaLink);

        try{
            Document doc = Jsoup.connect(String.valueOf(fullUrl)).get();
            Elements elements = doc.getElementsByTag("script");
            Element scriptElement = elements.first();

            String script = scriptElement.data();
            Result res = new Result(script, doc);
            return res;
        }
        catch (Exception e){
            logger.error("Couldn't scrape url: {}", e.getMessage());
            return null;
        }
    }

    public FoodRecipeDto getFoodRecipe(String script, Document doc){
        Map<String, Object> mappedScript = Util.convertJsonToMap(script);
        FoodRecipeDto foodRecipeDto = new FoodRecipeDto();
        foodRecipeDto.setRecipeName((String) mappedScript.get("name"));
        Elements recipeDetailElements = doc.select("div.ContentRecipe_recipeDetail__0EBU0 span");
        Element first = null;
        Element second = null;
        Element third = null;
        if(recipeDetailElements.size() > 2){
            first = recipeDetailElements.get(0); //to get first
            second = recipeDetailElements.get(1); //to get second
            third = recipeDetailElements.get(2); //to get third
        }
        foodRecipeDto.setServesFor(Util.extractNumber(Objects.requireNonNull(first.text())));
        foodRecipeDto.setCookingTime(Objects.requireNonNull(second.text()));
        foodRecipeDto.setPreparationTime(Objects.requireNonNull(third.text()));

        /*

        String[] parts = ingredientText.split("(\\d+\\s*(?:bardağı|adet|kaşığı|gram|paket|kase)\\s*)", 2);

        String amount = parts[0].trim();
        String ingredient = parts[1].trim();

         */
        return foodRecipeDto;
    }

    private static class Result {
        private boolean success;
        private String script;
        private Document document;
        private String error;

        public Result(boolean success, String script, Document document, String error) {
            this.success = success;
            this.script = script;
            this.document = document;
            this.error = error;
        }

        public Result(String script, Document document) {
            this.script = script;
            this.document = document;
        }

        public Result(){

        }

        public Result(boolean success, String script, Document document) {
            this(success, script, document, null);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getScript() {
            return script;
        }

        public Document getDocument() {
            return document;
        }

        public String getError() {
            return error;
        }
    }

}
