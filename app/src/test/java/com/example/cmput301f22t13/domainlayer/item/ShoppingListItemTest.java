package com.example.cmput301f22t13.domainlayer.item;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ShoppingListItemTest extends TestCase {

    @Test
    public void testAddIngredient() {
        GregorianCalendar gCal = new GregorianCalendar(0, 0, 0);
        IngredientItem ingredient = new IngredientItem(
                "Bacon",
                "pig bacon",
                2,
                "k/g",
                "Meat",
                gCal,
                "photoLink",
                "Fridge"
        );

        ShoppingListItem shoppingList = new ShoppingListItem();
        shoppingList.addIngredient(ingredient, 12);
        assertEquals(shoppingList.getShoppingList().size(), 1);
        assertEquals((int) shoppingList.getShoppingList().get(ingredient), 12);
    }

    @Test
    public void testRemoveIngredient() {
        GregorianCalendar gCal = new GregorianCalendar(0, 0, 0);
        IngredientItem ingredient = new IngredientItem(
                "Bacon",
                "pig bacon",
                2,
                "k/g",
                "Meat",
                gCal,
                "photoLink",
                "Fridge"
        );

        Map<IngredientItem, Integer> initialShoppingList = new HashMap<>();
        initialShoppingList.put(ingredient, 10);
        ShoppingListItem shoppingList = new ShoppingListItem(initialShoppingList);
        assertEquals(shoppingList.getShoppingList().size(), 1);
        assertEquals((int) shoppingList.getShoppingList().get(ingredient), 10);
        shoppingList.deleteIngredient(ingredient);
        assertEquals(shoppingList.getShoppingList().size(), 0);
        assertEquals(shoppingList.getShoppingList().get(ingredient), null);
    }

    @Test
    public void testIncrementIngredient() {
        GregorianCalendar gCal = new GregorianCalendar(0, 0, 0);
        IngredientItem ingredient = new IngredientItem(
                "Bacon",
                "pig bacon",
                2,
                "k/g",
                "Meat",
                gCal,
                "photoLink",
                "Fridge"
        );

        ShoppingListItem shoppingList = new ShoppingListItem();
        shoppingList.addIngredient(ingredient, 12);
        assertEquals((int) shoppingList.getShoppingList().get(ingredient), 12);
        shoppingList.incrementIngredient(ingredient);
        shoppingList.incrementIngredient(ingredient);
        assertEquals((int) shoppingList.getShoppingList().get(ingredient), 14);
        shoppingList.incrementIngredient(ingredient, 3);
        assertEquals((int) shoppingList.getShoppingList().get(ingredient), 17);
    }

    @Test
    public void testDecrementIngredient() {
        GregorianCalendar gCal = new GregorianCalendar(0, 0, 0);
        IngredientItem ingredient = new IngredientItem(
                "Bacon",
                "pig bacon",
                2,
                "k/g",
                "Meat",
                gCal,
                "photoLink",
                "Fridge"
        );

        ShoppingListItem shoppingList = new ShoppingListItem();
        shoppingList.addIngredient(ingredient, 12);
        assertEquals((int) shoppingList.getShoppingList().get(ingredient), 12);
        shoppingList.decrementIngredient(ingredient);
        shoppingList.decrementIngredient(ingredient);
        assertEquals((int) shoppingList.getShoppingList().get(ingredient), 10);
        shoppingList.decrementIngredient(ingredient, 3);
        assertEquals((int) shoppingList.getShoppingList().get(ingredient), 7);
    }
}
