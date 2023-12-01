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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ScrapeService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ScrapeService.class);

    public FoodRecipeDto scrapeAndCreateNewFoodRecipe(String foodName) {
        String permaLink = this.searchFoodRecipeUrl(foodName);
        String recipe = this.scrapeUrl("https://www.yemek.com", permaLink);
        return this.getFoodRecipe(recipe);
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

    public String scrapeUrl(String url, String permaLink){
        StringBuilder fullUrl = new StringBuilder(url);
        fullUrl.append(permaLink);

        try{
            Document doc = Jsoup.connect(String.valueOf(fullUrl)).get();
            Elements elements = doc.getElementsByTag("script");
            Element scriptElement = elements.first();

            assert scriptElement != null;
            return scriptElement.data();
        }
        catch (Exception e){
            logger.error("Couldn't scrape url: {}", e.getMessage());
            return null;
        }
    }

    public FoodRecipeDto getFoodRecipe(String script){
        Map<String, Object> mappedScript = Util.convertJsonToMap(script);
        FoodRecipeDto foodRecipeDto = new FoodRecipeDto();
        foodRecipeDto.setRecipeName((String) mappedScript.get("name"));
        foodRecipeDto.setServesFor(Integer.parseInt((String) mappedScript.get("recipeYield")));
        foodRecipeDto.setCookingTime(Util.parseDuration((String) mappedScript.get("cookTime")));
        foodRecipeDto.setPreparationTime(Util.parseDuration((String) mappedScript.get("prepTime")));

        List<List<String>> recipeIngredients = (List<List<String>>)mappedScript.get("recipeIngredient");
        HashMap<Integer, ArrayList<String>> ingredientsMap = getIngredientsMap(recipeIngredients);
        foodRecipeDto.setIngredients(ingredientsMap);

        List<List<String>> recipeInstructions = (List<List<String>>)mappedScript.get("recipeInstructions");
        HashMap<Integer, String> instructionsMap = getInstructionsMap(recipeInstructions);
        foodRecipeDto.setInstructions(instructionsMap);

        return foodRecipeDto;
    }

    private HashMap<Integer, ArrayList<String>> getIngredientsMap(List<List<String>> recipeIngredients) {
        HashMap<Integer, ArrayList<String>> ingredientsMap = new HashMap<>();
        int ingredientIndex = 0;
        for (List<String> ingredientList : recipeIngredients) {
            for (String ingredient : ingredientList) {
                ArrayList<String> ingredients = new ArrayList<>();
                Pattern pattern = Pattern.compile("(.*?\\s(?:bardağı|adet|kaşığı|gram|paket|kase|diş))\\s*(.*)");
                Matcher matcher = pattern.matcher(ingredient);

                if (matcher.find()) {
                    ingredients.add(matcher.group(1).trim());
                    ingredients.add(matcher.group(2).trim());
                }
                ingredientsMap.put(ingredientIndex, ingredients);
                ingredientIndex++;
            }
        }
        return ingredientsMap;
    }

    private HashMap<Integer, String> getInstructionsMap(List<List<String>> recipeInstructions){
        HashMap<Integer, String> instructionMap = new HashMap<>();
        int instructionIndex = 0;
        for (List<String> instructionList : recipeInstructions) {
            for (String instruction : instructionList) {
                Pattern pattern = Pattern.compile("(.*?[.!?])");
                Matcher matcher = pattern.matcher(instruction);

                while (matcher.find()) {
                    instructionMap.put(instructionIndex, matcher.group(1).trim());
                }
                instructionIndex += 1;
            }
        }
        return instructionMap;
    }

}
