package com.example.cmput301f22t13.domainlayer.item;

import java.util.HashMap;
import java.util.Map;

public class ShoppingListItem {

    private Map<IngredientItem, Integer> shoppingList;

    public ShoppingListItem() {
        this.shoppingList = new HashMap<>();
    }

    /**
     * Constructor to create shopping list
     * @param shoppingList Initial shopping list
     */
    public ShoppingListItem(Map<IngredientItem, Integer> shoppingList) {
        this.shoppingList = shoppingList;
    }

    /**
     * Getter for shoppingList
     * @return Current shopping list
     */
    public Map<IngredientItem, Integer> getShoppingList() {
        return shoppingList;
    }

    /**
     * Add ingredient to shopping list
     * @param ingredientItem Ingredient to add
     * @param count How many ingredients
     */
    public void addIngredient(IngredientItem ingredientItem, Integer count) {
        this.shoppingList.put(ingredientItem, count);
    }

    /**
     * Delete ingredient from shopping list
     * @param ingredientItem Ingredient to delete
     */
    public void deleteIngredient(IngredientItem ingredientItem) {
        this.shoppingList.remove(ingredientItem);
    }

    /**
     * Increase the count of an ingredient in shopping list
     * @param ingredientItem Ingredient to increment
     */
    public void incrementIngredient(IngredientItem ingredientItem) {
        Integer currentCount = this.shoppingList.get(ingredientItem);
        this.shoppingList.put(ingredientItem, currentCount + 1);
    }

    /**
     * Increase the count of an ingredient in shopping list by number
     * @param ingredientItem Ingredient to increment
     * @param incrementer Number to increment by
     */
    public void incrementIngredient(IngredientItem ingredientItem, int incrementer) {
        Integer currentCount = this.shoppingList.get(ingredientItem);
        this.shoppingList.put(ingredientItem, currentCount + incrementer);
    }

    /**
     * Decrease the count of an ingredient in shopping list
     * @param ingredientItem Ingredient to decrement
     */
    public void decrementIngredient(IngredientItem ingredientItem) {
        Integer currentCount = this.shoppingList.get(ingredientItem);
        this.shoppingList.put(ingredientItem, currentCount - 1);
    }

    /**
     * Decrease the count of an ingredient in shopping list by number
     * @param ingredientItem Ingredient to decrement
     * @param decrementer Number to decrease by
     */
    public void decrementIngredient(IngredientItem ingredientItem, int decrementer) {
        Integer currentCount = this.shoppingList.get(ingredientItem);
        this.shoppingList.put(ingredientItem, currentCount - decrementer);
    }
}
