package com.example.cmput301f22t13.domainlayer.item;

import java.util.HashMap;
import java.util.Map;

public class CountedIngredient {

    private IngredientItem ingredient;
    private int count;

    public CountedIngredient() {
        this.ingredient = new IngredientItem();
        this.count = 0;
    }

    public CountedIngredient(IngredientItem ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }


    public void incrementIngredient() {
        this.count += 1;
    }

    public void incrementIngredient(int incrementer) {
        this.count += incrementer;
    }

    public void decrementIngredient() {
        this.count -= 1;
    }

    public void decrementIngredient(int decrementer) {
        this.count -= decrementer;
    }

    public IngredientItem getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientItem ingredient) {
        this.ingredient = ingredient;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
