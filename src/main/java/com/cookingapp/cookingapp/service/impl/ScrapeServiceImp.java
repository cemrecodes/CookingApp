package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.dto.IngredientDto;
import com.cookingapp.cookingapp.dto.InstructionDto;
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

    private final String ingredientAmountSeperators = "bardağı|adet|kaşığı|gram|paket|kase|diş|litre|yaprak|mililitre|ml.|paket|tutam|demet|avuç|baş|dal";

    @Override
    public RecipeDto scrapeAndCreateNewRecipe(String foodName) {
        // find endpoint for recipe
        String permaLink = this.searchRecipeUrl(foodName);
        if(permaLink == null){
            return null;
        }
        // append endpoint to base url and scrape the recipe
        HashMap<String, Object> recipeStr = this.scrapeUrl("https://www.yemek.com", permaLink);
        // convert recipe hashmap to dto
        RecipeDto recipeDto = this.getRecipe(recipeStr);
        // convert recipe dto to recipe and save
        Recipe recipe = this.recipeService.save(recipeDto.convertToRecipe());
        // convert ingredient dto's to ingredient and save
        List<Ingredient> ingredients = recipeDto.getIngredients().stream().map(IngredientDto::toIngredient).toList();
        for(Ingredient ingredient: ingredients){
            ingredient.setRecipe(recipe);
            this.ingredientService.save(ingredient);
        }
        // add id to recipedto for response
        recipeDto.setId(recipe.getId());
        return recipeDto;
    }

    /* returns endpoint for recipe to be scraped */
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

        logger.info("ScrapeServiceImp -> searchRecipeUrl permalink: {}", firstPermalink);
        return firstPermalink;
    }

    /* returns recipe by scraping */
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

    /* returns RecipeDto converted from recipe hashmap */
    @Override
    public RecipeDto getRecipe(HashMap<String, Object> recipeMap) {
        Map<String, Object> mappedScript = Util.convertJsonToMap((String) recipeMap.get("recipe"));

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setRecipeName((String) mappedScript.get("name"));
        recipeDto.setImageUrl((String) Util.convertObjectToList(mappedScript.get("image")).get(0));
        recipeDto.setServesFor((String) recipeMap.get("servesFor"));
        // converts cookTime to x saat, x dakika format
        recipeDto.setCookingTime(mappedScript.get("cookTime") != null ? Util.parseDuration((String) mappedScript.get("cookTime")) : "0 dakika");
        // converts preptTime to x saat, x dakika format
        recipeDto.setPreparationTime(Util.parseDuration((String) mappedScript.get("prepTime")));
        // gets total time ( cookTime + prepTime )
        recipeDto.setTotalTime(Util.getTotalTime(mappedScript.get("cookTime") != null ? (String) mappedScript.get("cookTime") : "0M", (String) mappedScript.get("prepTime") ));

        // converts ingredients to IngredientDto array
        List<List<String>> recipeIngredients = (List<List<String>>) mappedScript.get("recipeIngredient");
        ArrayList<IngredientDto> ingredientsArray = getIngredientsArray(recipeIngredients);
        recipeDto.setIngredients(ingredientsArray);

        // finds cooking - prep times for timer and converts instructions to InstructionDto array
        List<List<String>> recipeInstructions = (List<List<String>>) mappedScript.get("recipeInstructions");
        ArrayList<InstructionDto> instructionDtos = getInstructionsArray(recipeInstructions);
        recipeDto.setInstructions(instructionDtos);

        // get difficulty and category of the recipe
        Map<String, Object> difficultyAndCategory = this.getDifficultyAndCategory(recipeDto);
        recipeDto.setDifficultyLevel((String) difficultyAndCategory.get("difficulty"));
        recipeDto.setCategory((String) difficultyAndCategory.get("category"));

        return recipeDto;
    }

    private ArrayList<IngredientDto> getIngredientsArray(List<List<String>> recipeIngredients){
        ArrayList<IngredientDto> ingredientsArray = new ArrayList<>();

        for (List<String> ingredientList : recipeIngredients) {
            for (String ingredient : ingredientList) {
                IngredientDto ingredientDto = new IngredientDto();
                Pattern pattern = Pattern.compile("(.*?\\s(?:" + ingredientAmountSeperators + "))\\s*(.*)");
                Matcher matcher = pattern.matcher(ingredient);

                if (matcher.find()) {
                    ingredientDto.setAmount(Util.removeExtraSpaces(matcher.group(1)));
                    ingredientDto.setIngredient(Util.removeExtraSpaces(matcher.group(2)));
                }
                ingredientsArray.add(ingredientDto);
            }
        }
        return ingredientsArray;
    }

    private ArrayList<InstructionDto> getInstructionsArray(List<List<String>> recipeInstructions) {
        ArrayList<InstructionDto> instructionsArray = new ArrayList<>();

        // actually one instructionList is returned and it has ArrayList<String>
        // which means first for loop is looped just once
        for (List<String> instructionList : recipeInstructions) {
            for(String instruction: instructionList) {
                InstructionDto instructionDto = new InstructionDto();
                instructionDto.setInstruction(Util.removeTags(instruction));
                // finding times for timer on frontend side
                instructionDto.setTime(Util.findPrepOrCookTime(instruction));
                instructionsArray.add(instructionDto);
            }
        }
        return instructionsArray;
    }


    private Map<String, Object> getDifficultyAndTerms(RecipeDto recipe) {
        String chatGptResponse = this.chatGptService.getDifficultyLevelAndTerms(recipe);
        Map<String, Object> mappedChatGptResponse = Util.convertJsonToMap(chatGptResponse);
        return mappedChatGptResponse;
    }

    /* gets difficulty and category of recipe from chatgpt */
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
