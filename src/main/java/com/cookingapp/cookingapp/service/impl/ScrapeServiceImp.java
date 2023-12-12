package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.entity.Ingredient;
import com.cookingapp.cookingapp.entity.Recipe;
import com.cookingapp.cookingapp.service.ChatGptService;
import com.cookingapp.cookingapp.service.IngredientService;
import com.cookingapp.cookingapp.service.RecipeService;
import com.cookingapp.cookingapp.service.ScrapeService;
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
public class ScrapeServiceImp implements ScrapeService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ScrapeServiceImp.class);

    private final RecipeService recipeService;

    private final ChatGptService chatGptService;

    private final IngredientService ingredientService;

    private final String ingredientAmountSeperators = "bardağı|adet|kaşığı|gram|paket|kase|diş|litre|yaprak|mililitre|ml.|paket|tutam";

    @Override
    public RecipeDto scrapeAndCreateNewRecipe(String foodName) {
        String permaLink = this.searchRecipeUrl(foodName);
        HashMap<String, Object> recipeStr = this.scrapeUrl("https://www.yemek.com", permaLink);
        RecipeDto recipeDto = this.getRecipe(recipeStr);
        Recipe recipe = this.recipeService.save(recipeDto.convertToRecipe());
        List<Ingredient> ingredients = this.ingredientService.processIngredientsHashMap(recipeDto.getIngredients(), recipe);
        for(Ingredient ingredient: ingredients){
            this.ingredientService.save(ingredient);
        }
        recipeDto.setId(recipe.getId());
        return recipeDto;
    }

    @Override
    public String searchRecipeUrl(String foodName) {
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
        } catch (Exception e) {
            logger.error("Couldn't get response: {}", e.getMessage());
        }

        return firstPermalink;
    }

    @Override
    public HashMap<String, Object> scrapeUrl(String url, String permaLink) {
        StringBuilder fullUrl = new StringBuilder(url);
        fullUrl.append(permaLink);
        HashMap<String, Object> result = new HashMap<>();

        try {
            Document doc = Jsoup.connect(String.valueOf(fullUrl)).get();

            Element h3Element = doc.select("h3:contains(KAÇ KİŞİLİK)").first();

            if (h3Element != null) {
                Element spanElement = h3Element.nextElementSibling().select("span").first();
                if (spanElement != null) {
                    String spanText = spanElement.text();
                    result.put("servesFor", spanText);
                }
            }

            Elements elements = doc.getElementsByTag("script");
            Element scriptElement = elements.first();

            assert scriptElement != null;
            result.put("recipe", scriptElement.data());

            return result;
        } catch (Exception e) {
            logger.error("Couldn't scrape url: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public RecipeDto getRecipe(HashMap<String, Object> recipeMap) {
        Map<String, Object> mappedScript = Util.convertJsonToMap((String) recipeMap.get("recipe"));

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setRecipeName((String) mappedScript.get("name"));
        recipeDto.setImageUrl((String) Util.convertObjectToList(mappedScript.get("image")).get(0));
        recipeDto.setServesFor((String) recipeMap.get("servesFor"));
        recipeDto.setCookingTime(mappedScript.get("cookTime") != null ? Util.parseDuration((String) mappedScript.get("cookTime")) : "0 dakika");
        recipeDto.setPreparationTime(Util.parseDuration((String) mappedScript.get("prepTime")));
        recipeDto.setTotalTime(Util.getTotalTime(mappedScript.get("cookTime") != null ? (String) mappedScript.get("cookTime") : "0M", (String) mappedScript.get("prepTime") ));

        List<List<String>> recipeIngredients = (List<List<String>>) mappedScript.get("recipeIngredient");
        HashMap<Integer, ArrayList<String>> ingredientsMap = getIngredientsMap(recipeIngredients);
        recipeDto.setIngredients(ingredientsMap);

        List<List<String>> recipeInstructions = (List<List<String>>) mappedScript.get("recipeInstructions");
        HashMap<Integer, String> instructionsMap = getInstructionsMap(recipeInstructions);
        recipeDto.setInstructions(instructionsMap);

        Map<String, Object> difficultyAndCategory = this.getDifficultyAndCategory(recipeDto);

        recipeDto.setDifficultyLevel((String) difficultyAndCategory.get("difficulty"));
        recipeDto.setCategory((String) difficultyAndCategory.get("category"));

        /*
        Map<String, Object> difficultyAndTerms = this.getDifficultyAndTerms(foodRecipeDto);

        foodRecipeDto.setDifficultyLevel(String.valueOf(difficultyAndTerms.get("difficulty")));

         */
//        if( Util.convertObjectToList(difficultyAndTerms.get("terms")).size() != 0 ){
        /*
            Map<String, Object> termList = (Map<String, Object>) difficultyAndTerms.get("terms");
            HashMap<Integer, ArrayList<String>> terms = this.getTermsMap(termList);
            foodRecipeDto.setTerms(terms);

         */
  //      }

        return recipeDto;
    }

    private HashMap<Integer, ArrayList<String>> getIngredientsMap(List<List<String>> recipeIngredients) {
        HashMap<Integer, ArrayList<String>> ingredientsMap = new HashMap<>();
        int ingredientIndex = 0;
        for (List<String> ingredientList : recipeIngredients) {
            for (String ingredient : ingredientList) {
                ArrayList<String> ingredients = new ArrayList<>();
                Pattern pattern = Pattern.compile("(.*?\\s(?:" + ingredientAmountSeperators + "))\\s*(.*)");
                Matcher matcher = pattern.matcher(ingredient);

                if (matcher.find()) {
                    ingredients.add(Util.removeExtraSpaces(matcher.group(1)));
                    ingredients.add(Util.removeExtraSpaces(matcher.group(2)));
                }
                ingredientsMap.put(ingredientIndex, ingredients);
                ingredientIndex++;
            }
        }
        return ingredientsMap;
    }

    private HashMap<Integer, String> getInstructionsMap(List<List<String>> recipeInstructions) {
        HashMap<Integer, String> instructionMap = new HashMap<>();
        int instructionIndex = 0;
        for (List<String> instructionList : recipeInstructions) {
            for (String instruction : instructionList) {
                Pattern pattern = Pattern.compile("(.*?[.!?])");
                Matcher matcher = pattern.matcher(instruction);

                while (matcher.find()) {
                    String instructionToBeAdded = Util.removeTags(matcher.group(1));
                    // instructionMap.put(instructionIndex, Util.removeExtraSpaces(matcher.group(1)));
                    instructionMap.put(instructionIndex, instructionToBeAdded);
                }
                instructionIndex += 1;
            }
        }
        return instructionMap;
    }

    private Map<String, Object> getDifficultyAndTerms(RecipeDto recipe) {
        String chatGptResponse = this.chatGptService.getDifficultyLevelAndTerms(recipe);
        Map<String, Object> mappedChatGptResponse = Util.convertJsonToMap(chatGptResponse);
        return mappedChatGptResponse;
    }

    private Map<String, Object> getDifficultyAndCategory(RecipeDto recipe) {
        String chatGptResponse = this.chatGptService.getDifficultyLevelAndCategory(recipe);
        return Util.convertJsonToMap(chatGptResponse);
    }

    @Override
    public Map<String, Object> getTerms(RecipeDto recipe) {
        String chatGptResponse = this.chatGptService.getTerms(recipe);
        Map<String, Object> mappedChatGptResponse = Util.convertJsonToMap(chatGptResponse);
        return mappedChatGptResponse;
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getTermsMap(Map<String, Object> terms) {
        HashMap<Integer, ArrayList<String>> termsMap = new HashMap<>();
        int termIndex = 0;

        ArrayList<String> termList = (ArrayList<String>) terms.get(Integer.toString(termIndex));
        while ( termList != null){
            System.out.println( termList );
            termsMap.put(termIndex, termList);
            termIndex += 1;
            termList = (ArrayList<String>) terms.get(Integer.toString(termIndex));
        }
        return termsMap;
    }

}
