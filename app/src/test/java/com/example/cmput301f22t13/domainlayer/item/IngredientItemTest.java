package com.example.cmput301f22t13.domainlayer.item;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.GregorianCalendar;

public class IngredientItemTest extends TestCase {
    @Test
    public void testCreatingEmptyIngredient() {
        IngredientItem newItem = new IngredientItem();
        int amount = newItem.getAmount();
        assertEquals(amount, 0);
        assertEquals(newItem.getDescription(), "");
        assertEquals(newItem.getCategory(), "");
        assertEquals(newItem.getBbd(), new GregorianCalendar(0,0,0));
        assertEquals(newItem.getName(), "");
        assertEquals(newItem.getHashId(), "");
        assertEquals(newItem.getUnit(), "");
        assertEquals(newItem.getLocation(), "");
        assertEquals(newItem.getPhoto(), "");
    }

    @Test
    public void testCreatingIngredient() {
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

        int amount = newItem.getAmount();
        assertEquals(amount, 69);
        assertEquals(newItem.getDescription(), "Chicken that is made from butter");
        assertEquals(newItem.getCategory(), "indian");
        assertEquals(newItem.getBbd(), new GregorianCalendar(1,1,1));
        assertEquals(newItem.getName(), "Butter Chicken");
        assertEquals(newItem.getUnit(), "idk a unit");
        assertEquals(newItem.getLocation(), "In my tummy");
        assertEquals(newItem.getPhoto(), "Link to a photo");
    }
}
