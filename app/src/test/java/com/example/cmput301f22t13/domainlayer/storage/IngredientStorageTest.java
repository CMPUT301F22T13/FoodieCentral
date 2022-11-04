package com.example.cmput301f22t13.domainlayer.storage;

import com.example.cmput301f22t13.domainlayer.item.IngredientItem;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class IngredientStorageTest extends TestCase {
    public void testIngredientsAreEmpty() {
        IngredientStorage storage = new IngredientStorage();
        ArrayList<IngredientItem> ingredientItems = storage.getIngredients();

        assertEquals(ingredientItems.isEmpty(), true);
        assertEquals(ingredientItems.size(), 0);
    }

    public void testAddIngredients() {
        IngredientStorage storage = new IngredientStorage();
        IngredientItem newItem = new IngredientItem(
                "Butter Chicken",
                "Chicken that is made from butter",
                69,
                "idk a unit",
                "indian",
                new GregorianCalendar(1,1,1),
                "Link to a photo",
                "In my tummy"
        );
        storage.addIngredientToStorage(newItem);

        assertEquals(storage.getIngredients().isEmpty(), false);
        assertEquals(storage.getIngredients().size(), 1);
    }

    public void testRemoveIngredientByObj() {
        IngredientStorage storage = new IngredientStorage();
        IngredientItem newItem = new IngredientItem(
                "Butter Chicken",
                "Chicken that is made from butter",
                69,
                "idk a unit",
                "indian",
                new GregorianCalendar(1,1,1),
                "Link to a photo",
                "In my tummy"
        );
        storage.addIngredientToStorage(newItem);

        assertEquals(storage.getIngredients().isEmpty(), false);
        assertEquals(storage.getIngredients().size(), 1);

        storage.removeIngredientFromStorage(newItem);
        assertEquals(storage.getIngredients().isEmpty(), true);
        assertEquals(storage.getIngredients().size(), 0);
    }

    public void testRemoveIngredientByIndex() {
        IngredientStorage storage = new IngredientStorage();
        IngredientItem newItem = new IngredientItem(
                "Butter Chicken",
                "Chicken that is made from butter",
                69,
                "idk a unit",
                "indian",
                new GregorianCalendar(1,1,1),
                "Link to a photo",
                "In my tummy"
        );
        storage.addIngredientToStorage(newItem);

        assertEquals(storage.getIngredients().isEmpty(), false);
        assertEquals(storage.getIngredients().size(), 1);

        storage.removeIngredientFromStorage(0);
        assertEquals(storage.getIngredients().isEmpty(), true);
        assertEquals(storage.getIngredients().size(), 0);
    }


}
