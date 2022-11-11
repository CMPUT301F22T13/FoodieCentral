package com.example.cmput301f22t13.domainlayer.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**Not yet implemented - will store data regarding mealplans
 * */
public class MealPlanItem implements Serializable {
    private ArrayList<IngredientItem> ingredients;
    private ArrayList<RecipeItem> recipes;
    private Integer servings;
    private GregorianCalendar week;

    public MealPlanItem() {
        this.ingredients = new ArrayList<IngredientItem>();
        this.recipes = new ArrayList<RecipeItem>();
        this.servings = -1;
        this.week = new GregorianCalendar();
    }

    public MealPlanItem(ArrayList<IngredientItem> ingredients, ArrayList<RecipeItem> recipes, Integer servings, GregorianCalendar week) {
        this.ingredients = ingredients;
        this.recipes = recipes;
        this.servings = servings;
        this.week = week;
    }

    public ArrayList<IngredientItem> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<IngredientItem> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<RecipeItem> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<RecipeItem> recipes) {
        this.recipes = recipes;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public GregorianCalendar getWeek() {
        return week;
    }

    public void setWeek(GregorianCalendar week) {
        this.week = week;
    }
}
