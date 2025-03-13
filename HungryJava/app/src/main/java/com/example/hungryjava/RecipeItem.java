package com.example.hungryjava;

public class RecipeItem {
    private final String imageName;
    private final String caption;

    private String recipe_id;


    public RecipeItem(String imageName, String caption) {
        this.imageName = imageName;
        this.caption = caption;
    }

    public String getImageName() {
        return imageName;
    }

    public String getRecipeId() {
        return recipe_id;
    }
    public String getCaption() {
        return caption;
    }
}

