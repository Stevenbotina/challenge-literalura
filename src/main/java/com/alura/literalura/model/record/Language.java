package com.alura.literalura.model.record;

public enum Language {
    en("[en]", "English"),
    es("[es]", "Spanish"),
    fr("[fr]", "French"),
    pt("[pt]", "Portuguese");

    private String languageGutendex;
    private String languageEnglish;

    Language(String LanguageGutendex, String LanguageEnglish) {
        this.languageGutendex = LanguageGutendex;
        this.languageEnglish = LanguageEnglish;

    }

    public static Language fromString(String text) {
        for (Language language : Language.values()) {
            if (language.languageGutendex.equalsIgnoreCase(text)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Language not found: " + text);
    }

    public static Language fromEnglish(String text) {
        for (Language language : Language.values()) {
            if (language.languageEnglish.equalsIgnoreCase(text)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Language not found: " + text);
    }

    public String getLanguageGutendex() {
        return languageGutendex;
    }

    public String getLanguageEnglish() {
        return languageEnglish;
    }
}
