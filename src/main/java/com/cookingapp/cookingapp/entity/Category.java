package com.cookingapp.cookingapp.entity;

import com.cookingapp.cookingapp.util.Util;

public enum Category {
    SOUP,
    MAIN_DISH,
    DESSERT,
    DRINK;

    public static Category convert(String category) {
        return switch (Util.translateCategoryToEnglish(category)) {
            case "SOUP" -> SOUP;
            case "MAIN DISH" -> MAIN_DISH;
            case "DESSERT" -> DESSERT;
            case "DRINK" -> DRINK;
            default -> throw new IllegalArgumentException("Geçersiz kategori: " + category);
        };
    }

    public static String toString(Category category){
        return switch (category) {
            case SOUP -> "çorba";
            case MAIN_DISH -> "ana yemek";
            case DESSERT -> "tatlı";
            case DRINK -> "içecek";
        };
    }
}
