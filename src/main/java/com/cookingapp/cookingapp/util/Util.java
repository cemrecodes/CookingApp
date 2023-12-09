package com.cookingapp.cookingapp.util;

import com.cookingapp.cookingapp.entity.DifficultyLevel;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.swing.plaf.IconUIResource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Util {
    public static String removeExtraSpaces(String input) {
        // Replace multiple consecutive spaces with a single space
        return input.replaceAll("\\s+", " ");
    }

    public static String removeSpaces(String input){
        return input.replace(" ", "");
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
        Pattern pattern = Pattern.compile("\\s*(\\d+)\\s*");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            String numberStr = matcher.group(1);
            return Integer.parseInt(numberStr);
        }
        return 0;
    }

    public static String parseDuration(String duration) {
        int hoursIndex = duration.indexOf("H");
        int minutesIndex = duration.indexOf("M");

        int hours = 0;
        int minutes = 0;

        if (hoursIndex != -1) {
            hours = Integer.parseInt(duration.substring(2, hoursIndex));
            return hours + " saat ";
        }

        if (minutesIndex != -1) {
            if (hoursIndex != -1) {
                minutes = Integer.parseInt(duration.substring(hoursIndex + 1, minutesIndex));
            } else {
                minutes = Integer.parseInt(duration.substring(2, minutesIndex));
            }
            return minutes + " dakika";
        }

        return null;
    }

    public static int convertToMinutes(String duration) {
        if (duration.contains("H") || duration.contains("saat")){
            int hours = extractNumber(duration);
            return hours *  60;
        }
        else if(duration.contains("M") || duration.contains("dakika")){
            return extractNumber(duration);
        }

        return 0;
    }

    public static String translateDifficultyLevelToTurkish(DifficultyLevel level){
        switch (level){
            case EASY -> {
                return "Kolay";
            }
            case MEDIUM ->
            {
                return "Orta";
            }
            case HARD -> {
                return "Zor";
            }
            default -> {
                return "Belirtilmemiş";
            }
        }
    }

    public static String translateDifficultyLevelToEnglish(String difficultyLevel){
        switch (difficultyLevel.toLowerCase()){
            case "kolay" -> {
                return "EASY";
            }
            case "orta" ->
            {
                return "MEDIUM";
            }
            case "zor" -> {
                return "HARD";
            }
            default -> {
                return "NOT SPECIFIED";
            }
        }
    }

    public static String translateCategoryToEnglish(String category){
        switch(category.toLowerCase()){
            case "çorba" -> {
                return "SOUP";
            }
            case "ana yemek" -> {
                return "MAIN DISH";
            }
            case "tatlı" -> {
                return "DESSERT";
            }
            case "içecek" -> {
                return "DRINK";
            }
            default -> {
                return "NOT SPECIFIED";
            }
        }
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }

    public static String getTotalTime(String cookingTime, String preparationTime){
        return Util.convertToMinutes(cookingTime) + Util.convertToMinutes(preparationTime) + " dakika";
    }

    public static String splitByDashAndUpperCaseInitials(String foodName){
        StringBuilder result = new StringBuilder();

        String[] words = foodName.split("-");
        for (String word: words) {
            String upperCased = word.substring(0, 1).toUpperCase() + word.substring(1);
            result.append(upperCased).append(" ");
        }
        result.setLength(result.length() - 1);
        return result.toString();
    }

}
