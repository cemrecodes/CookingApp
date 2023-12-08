package com.cookingapp.cookingapp.entity;

import com.cookingapp.cookingapp.util.Util;

public enum DifficultyLevel {
    EASY,
    MEDIUM,
    HARD;

    public static DifficultyLevel convert(String difficultyLevel) {
        return switch (Util.translateDifficultyLevelToEnglish(difficultyLevel)) {
            case "EASY" -> EASY;
            case "MEDIUM" -> MEDIUM;
            case "HARD" -> HARD;
            default -> throw new IllegalArgumentException("Geçersiz zorluk seviyesi: " + difficultyLevel);
        };
    }
}



