package com.cookingapp.cookingapp.util;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Map;

@Component
public class Util {
    public static String removeExtraSpaces(String input) {
        // Replace multiple consecutive spaces with a single space
        return input.replaceAll("\\s+", " ");
    }

    public static String addNewlineAfterSentenceEnds(String input) {
        // Split the input string into sentences based on common sentence-ending punctuation
        String[] sentences = input.split("[.!?]\\s*");

        // Join the sentences with "\n" added after each sentence end
        return String.join("\n", sentences);
    }

    public static String[] convertStringToArray(String input) {
        // Split the input string using "\n" as the separator
        return input.split("\\s*\\n\\s*");
    }

    public static Map<String, Object> convertJsonToMap(String jsonString) {
        Type mapType = com.google.gson.reflect.TypeToken.getParameterized(Map.class, String.class, Object.class).getType();
        return new Gson().fromJson(jsonString, mapType);
    }

    public static String turkishCharsToEnglish(String str) {
        if (str == null) {
            return null;
        }

        str = str.toLowerCase(); // Metni küçük harfe çevir

        str = str.replace("ı", "i");
        str = str.replace("ğ", "g");
        str = str.replace("ü", "u");
        str = str.replace("ş", "s");
        str = str.replace("ö", "o");
        str = str.replace("ç", "c");

        return str;
    }

    public static int extractNumber(String text) {
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (word.matches("\\d+")) {
                return Integer.parseInt(word);
            }
        }
        return 0; // Varsayılan değer, sayı bulunamazsa
    }

}
