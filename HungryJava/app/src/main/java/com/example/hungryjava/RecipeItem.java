package com.example.hungryjava;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
public class RecipeItem {
    private final String imageName;
    private final String caption;

    private String recipe_id;
    private boolean favorite;

    public RecipeItem(String id, String imageName, String caption, boolean favorite) {
        this.recipe_id = id;
        this.imageName = imageName;
        this.caption = caption;
        this.favorite = favorite;
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

    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}

