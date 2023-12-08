package com.cookingapp.cookingapp.service;

import com.cookingapp.cookingapp.dto.RecipeDto;
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

    private final RecipeService recipeService;

    private final ChatGptService chatGptService;

    public RecipeDto scrapeAndCreateNewRecipe(String foodName) {
        String permaLink = this.searchRecipeUrl(foodName);
        String recipeStr = this.scrapeUrl("https://www.yemek.com", permaLink);
        RecipeDto recipeDto = this.getRecipe(recipeStr);
        this.recipeService.save(recipeDto.convertToRecipe());
        return recipeDto;
    }

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

    public String scrapeUrl(String url, String permaLink) {
        StringBuilder fullUrl = new StringBuilder(url);
        fullUrl.append(permaLink);

        try {
            Document doc = Jsoup.connect(String.valueOf(fullUrl)).get();
            Elements elements = doc.getElementsByTag("script");
            Element scriptElement = elements.first();

            assert scriptElement != null;
            return scriptElement.data();
        } catch (Exception e) {
            logger.error("Couldn't scrape url: {}", e.getMessage());
            return null;
        }
    }

    public RecipeDto getRecipe(String script) {
        Map<String, Object> mappedScript = Util.convertJsonToMap(script);

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setRecipeName((String) mappedScript.get("name"));
        recipeDto.setImageUrl((String) Util.convertObjectToList(mappedScript.get("image")).get(0));
        recipeDto.setServesFor(Integer.parseInt((String) mappedScript.get("recipeYield")));
        recipeDto.setCookingTime(mappedScript.get("cookTime") != null ? Util.parseDuration((String) mappedScript.get("cookTime")) : "0 dakika");
        recipeDto.setPreparationTime(Util.parseDuration((String) mappedScript.get("prepTime")));
        recipeDto.setTotalTime(this.getTotalTime(mappedScript.get("cookTime") != null ? (String) mappedScript.get("cookTime") : "0M", (String) mappedScript.get("prepTime") ));

        List<List<String>> recipeIngredients = (List<List<String>>) mappedScript.get("recipeIngredient");
        HashMap<Integer, ArrayList<String>> ingredientsMap = getIngredientsMap(recipeIngredients);
        recipeDto.setIngredients(ingredientsMap);

        List<List<String>> recipeInstructions = (List<List<String>>) mappedScript.get("recipeInstructions");
        HashMap<Integer, String> instructionsMap = getInstructionsMap(recipeInstructions);
        recipeDto.setInstructions(instructionsMap);

        recipeDto.setDifficultyLevel(this.getDifficulty(recipeDto));

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
                Pattern pattern = Pattern.compile("(.*?\\s(?:bardağı|adet|kaşığı|gram|paket|kase|diş|litre))\\s*(.*)");
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
                    instructionMap.put(instructionIndex, Util.removeExtraSpaces(matcher.group(1)));
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

    private String getDifficulty(RecipeDto recipe) {
        String chatGptResponse = this.chatGptService.getDifficultyLevel(recipe);
        Map<String, Object> mappedChatGptResponse = Util.convertJsonToMap(chatGptResponse);
        return String.valueOf(mappedChatGptResponse.get("difficulty"));
    }

    public Map<String, Object> getTerms(RecipeDto recipe) {
        String chatGptResponse = this.chatGptService.getTerms(recipe);
        Map<String, Object> mappedChatGptResponse = Util.convertJsonToMap(chatGptResponse);
        return mappedChatGptResponse;
    }

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

    private String getTotalTime(String cookingTime, String preparationTime){
        return Util.convertToMinutes(cookingTime) + Util.convertToMinutes(preparationTime) + " dakika";
    }



}
