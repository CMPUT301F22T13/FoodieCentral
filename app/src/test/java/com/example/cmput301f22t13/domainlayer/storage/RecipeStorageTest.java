package com.example.cmput301f22t13.domainlayer.storage;

import com.example.cmput301f22t13.domainlayer.item.RecipeItem;

import junit.framework.TestCase;

import org.junit.Test;

public class RecipeStorageTest extends TestCase {
    private RecipeItem testItem = new RecipeItem();

    @Test
    public void testAddRecipeToStorage() {
        RecipeStorage rs = new RecipeStorage();
        rs.addRecipeToStorage(testItem);
        assertEquals(rs.getRecipes().contains(testItem), true);
    }

    @Test
    public void testRemoveRecipeFromStorage() {
        RecipeStorage rs = new RecipeStorage();
        rs.addRecipeToStorage(testItem);
        rs.removeRecipeFromStorage(testItem);
        assertEquals(rs.getRecipes().contains(testItem), false);
    }

    @Test
    public void testRemoveRecipeFromStorageIndex() {
        RecipeStorage rs = new RecipeStorage();
        rs.addRecipeToStorage(testItem);
        rs.removeRecipeFromStorage(0);
        assertEquals(rs.getRecipes().contains(testItem), false);
    }

    @Test
    public void testGetRecipes() {
        RecipeStorage rs = new RecipeStorage();
        rs.addRecipeToStorage(testItem);
        rs.removeRecipeFromStorage(0);
        // rs.addRecipeToStorage();
        assertEquals(rs.getRecipes(), false);
    }

}