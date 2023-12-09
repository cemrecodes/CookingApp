package com.cookingapp.cookingapp.service.impl;

import com.cookingapp.cookingapp.dto.ChatGptRequest;
import com.cookingapp.cookingapp.dto.ChatGptResponse;
import com.cookingapp.cookingapp.dto.Message;
import com.cookingapp.cookingapp.dto.RecipeDto;
import com.cookingapp.cookingapp.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptServiceImp implements ChatGptService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate template;

    private final String promptBase = "Aşağıdaki yemek tarifinin zorluğunu belirle(kolay,orta,zor)." +
            "Tarifte geçen bilinmedik yemek yapma terimlerini (Örneğin: benmari usulü, marine etmek, julienne doğrama gibi) varsa tespit et " +
            "ve kısa bir şekilde açıklamasını yap, " +
            "Tarifte özel terim bulunmayabilir, bu durumda terms'ü boş yolla." +
            "Cevabı dictionary şeklinde ver, zorluk derecesi key'i \"difficulty\", özel terimler key'i ise \"terms\" içerisinde key 0'an başlayarak sayı," +
            " value \"terim\", \"açıklama\" şeklinde liste olsun. Örnek: {\"terms:\":{\"0\":[\"terim\", \"açıklaması\"]}} ";


    @Override
    public String getDifficultyLevelAndTerms(RecipeDto recipe) {
        Message systemMessage = new Message("system", promptBase);
        Message exampleUserMessage = new Message("user", " Tarif: Mozaik Kek" +
               " Yumurta sarılarını ve şekerini karıştırarak iyice çırpın." +
               " Unu ekleyip karışımı pürüzsüz olana kadar karıştırın." +
               " Sütü ekleyip karışımı iyice çırpın." +
               " Karışımı benmari usulüyle, yani kaynar su dolu bir tencerenin üzerine oturtulmuş bir kap içinde sürekli karıştırarak pişirin." +
               " Karışım koyu bir kıvam aldığında ocaktan alın ve soğumaya bırakın." +
               " Soğuyan karışıma isteğe bağlı olarak vanilin veya vanilin şekeri ekleyin." +
               " Yumurta beyazlarını köpük haline gelene kadar çırpın." +
               " Yumurta beyazlarını hazırlanan karışıma yavaşça ekleyip spatula ile hafifçe karıştırın." +
               " Hazırlanan karışımı sufle kaplarına paylaştırın." +
               " Önceden ısıtılmış 180 derece fırında üzeri iyice kızarana kadar pişirin (yaklaşık 15-20 dakika).)");
        Message exampleAssistantMessage = new Message("assistant", "{ \"difficulty\": \"kolay\", " +
                "\"terms\": { \"0\": [\"benmari usulü\", \"Bir kabı sıcak suya oturtarak içindekini ısıtma veya eritme yöntemidir." +
                "Özellikle çikolata gibi doğrudan ateşe oturtulduğunda yanma veya kesilme tehlikesi olan yiyecekler için tercih edilir.\"]}} ");

        List<Message> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(exampleUserMessage);
        messages.add(exampleAssistantMessage);
        messages.add(new Message("user", recipe.toStringForChatGpt()) );
        ChatGptRequest request = new ChatGptRequest(model, messages);
        ChatGptResponse chatGptResponse = template.postForObject(apiUrl, request, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

    @Override
    public String getDifficultyLevelAndCategory(RecipeDto recipe) {
        Message systemMessage = new Message("system", "Aşağıdaki yemek tarifinin zorluğunu (kolay,orta,zor) ve kategorisini (çorba, ana yemek, tatlı, içecek) belirle." +
                "Cevabı dictionary şeklinde ver, zorluk derecesi key'i \"difficulty\", kategori keyi ise \"category\" olsun.");
        Message exampleUserMessage = new Message("user", " Tarif: Mozaik Kek" +
                " Yumurta sarılarını ve şekerini karıştırarak iyice çırpın." +
                " Unu ekleyip karışımı pürüzsüz olana kadar karıştırın." +
                " Sütü ekleyip karışımı iyice çırpın." +
                " Karışımı benmari usulüyle, yani kaynar su dolu bir tencerenin üzerine oturtulmuş bir kap içinde sürekli karıştırarak pişirin." +
                " Karışım koyu bir kıvam aldığında ocaktan alın ve soğumaya bırakın." +
                " Soğuyan karışıma isteğe bağlı olarak vanilin veya vanilin şekeri ekleyin." +
                " Yumurta beyazlarını köpük haline gelene kadar çırpın." +
                " Yumurta beyazlarını hazırlanan karışıma yavaşça ekleyip spatula ile hafifçe karıştırın." +
                " Hazırlanan karışımı sufle kaplarına paylaştırın." +
                " Önceden ısıtılmış 180 derece fırında üzeri iyice kızarana kadar pişirin (yaklaşık 15-20 dakika).)");
        Message exampleAssistantMessage = new Message("assistant", "{ \"difficulty\": \"kolay\", " +
                "\"category\": \"tatlı\" }");

        List<Message> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(exampleUserMessage);
        messages.add(exampleAssistantMessage);
        messages.add(new Message("user", "Tarif: " + recipe.toStringForChatGpt()) );
        ChatGptRequest request = new ChatGptRequest(model, messages);
        ChatGptResponse chatGptResponse = template.postForObject(apiUrl, request, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

    @Override
    public String getTerms(RecipeDto recipe) {
        Message systemMessage = new Message("system", "Yemek tarifinde geçen bilinmedik yemek yapma terimlerini (Örneğin: benmari usulü, marine etmek, julienne doğrama gibi) varsa tespit et " +
                "ve kısa bir şekilde açıklamasını yap, " +
                "Tarifte özel terim bulunmayabilir, bu durumda terms'ü boş yolla." +
                "Cevabı dictionary şeklinde ver, özel terimler key'i \"terms\" içerisinde key 0'an başlayarak sayı," +
                " value \"terim\", \"açıklama\" şeklinde liste olsun. Örnek: {\"terms:\":{\"0\":[\"terim\", \"açıklaması\"]}} ");
        Message exampleUserMessage = new Message("user", " Tarif: Mozaik Kek" +
                " Yumurta sarılarını ve şekerini karıştırarak iyice çırpın." +
                " Unu ekleyip karışımı pürüzsüz olana kadar karıştırın." +
                " Sütü ekleyip karışımı iyice çırpın." +
                " Karışımı benmari usulüyle, yani kaynar su dolu bir tencerenin üzerine oturtulmuş bir kap içinde sürekli karıştırarak pişirin." +
                " Karışım koyu bir kıvam aldığında ocaktan alın ve soğumaya bırakın." +
                " Soğuyan karışıma isteğe bağlı olarak vanilin veya vanilin şekeri ekleyin." +
                " Yumurta beyazlarını köpük haline gelene kadar çırpın." +
                " Yumurta beyazlarını hazırlanan karışıma yavaşça ekleyip spatula ile hafifçe karıştırın." +
                " Hazırlanan karışımı sufle kaplarına paylaştırın." +
                " Önceden ısıtılmış 180 derece fırında üzeri iyice kızarana kadar pişirin (yaklaşık 15-20 dakika).)");
        Message exampleAssistantMessage = new Message("assistant", "{ \"terms\": { \"0\": [\"benmari usulü\", \"Bir kabı sıcak suya oturtarak içindekini ısıtma veya eritme yöntemidir." +
                "Özellikle çikolata gibi doğrudan ateşe oturtulduğunda yanma veya kesilme tehlikesi olan yiyecekler için tercih edilir.\"]}} ");

        List<Message> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(exampleUserMessage);
        messages.add(exampleAssistantMessage);
        messages.add(new Message("user", recipe.toStringForChatGpt()) );
        ChatGptRequest request = new ChatGptRequest(model, messages);
        ChatGptResponse chatGptResponse = template.postForObject(apiUrl, request, ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

}
