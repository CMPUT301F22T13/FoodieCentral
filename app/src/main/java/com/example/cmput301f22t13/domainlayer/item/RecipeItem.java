package com.example.cmput301f22t13.domainlayer.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class RecipeItem implements Serializable {

    private String title;
    private int prepTime;
    private int servings;
    private String category;
    private String comments;
    private String photo;
    private ArrayList<IngredientItem> ingredients;

    /**
     * Constructor to create a recipe
     *
     * @param title Title of recipe
     * @param prepTime Time it takes to prepare the recipe
     * @param servings How many people the recipe ser
     * @param category What type of meal the recipe is making
     * @param comments Describing the recipe
     * @param photo ID that links to FireStorage instance
     * @param ingredients List of ingredients used in the recipe
     */
    public RecipeItem(
            String title,
            int prepTime,
            int servings,
            String category,
            String comments,
            String photo,
            ArrayList<IngredientItem> ingredients
    ) {
        this.title = title;
        this.prepTime = prepTime;
        this.servings = servings;
        this.category = category;
        this.comments = comments;
        this.photo = photo;
        this.ingredients = ingredients;
    }

    /**
     * Default constructor to create a recipe
     */
    public RecipeItem() {
        this.title = "";
        this.prepTime = -1;
        this.servings = -1;
        this.category = "";
        this.comments = "";
        this.photo = "";
        this.ingredients = new ArrayList<IngredientItem>();
    }

    /**
     * Get title of recipe
     * @return Title of recipe
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of a recipe
     * @param title Title of recipe
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the preparation time of a recipe
     * @return Preparation time of a recipe
     */
    public int getPrepTime() {
        return prepTime;
    }

    /**
     * Sets the preparation time of a recipe
     * @param prepTime Preparation time of a recipe
     */
    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    /**
     * Gets the number of people a recipe serves
     * @return Number of people a recipe serves
     */
    public int getServings() {
        return servings;
    }

    /**
     * Sets the number of people a recipe serves
     * @param servings The number of people a recipe serves
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    /**
     * Gets the category of a recipe
     * @return Category of a recipe
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of a recipe
     * @param category Category of a recipe
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the instructions of a recipe
     * @return Instructions of a recipe
     */
    public String getComments() {
        return comments;
    }

    /**
     * Setting the instructions of a recipe
     * @param comments Instructions for a recipe
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Gets the link to the recipe photo for FireStorage
     * @return Link to the recipe photo for FireStorage
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the link for the recipe photo to FireStorage
     * @param photo Link to photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Get the ingredients in a recipe
     * @return List of ingredients in a recipe
     */
    public ArrayList<IngredientItem> getIngredients() {
        return ingredients;
    }

    /**
     * Setting the ingredients of a recipe
     * @param ingredients List of ingredients in a recipe
     */
    public void setIngredients(ArrayList<IngredientItem> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Add an ingredient to ingredients of a recipe
     * @param ingredientItem ingredient to add to recipe
     */
    public void addIngredient(IngredientItem ingredientItem) {
        ingredients.add(ingredientItem);
    }

    /**
     * Delete an ingredient from a recipe by index
     * @param index index of the ingredient to delete
     */
    public void deleteIngredient(int index) {
        ingredients.remove(index);
    }

    /**
     * Delete an ingredient from a recipe by value
     * @param ingredientItem ingredient to delete
     */
    public void deleteIngredient(IngredientItem ingredientItem) {
        ingredients.remove(ingredientItem);
    }
}
