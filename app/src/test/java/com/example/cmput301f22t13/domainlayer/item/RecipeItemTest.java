package com.example.cmput301f22t13.domainlayer.item;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class RecipeItemTest extends TestCase {

    @Test
    public void testRecipeItemGettersAndSetters() {
        RecipeItem r = new RecipeItem();

        // Testing getters and setters
        r.setTitle("Bacon&Eggs");
        assertEquals(r.getTitle(), "Bacon&Eggs");

        r.setCategory("Breakfast");
        assertEquals(r.getCategory(), "Breakfast");

        r.setPrepTime(2);
        assertEquals(r.getPrepTime(), 2);

        r.setComments("This is my fav!");
        assertEquals(r.getComments(), "This is my fav!");

        // Will need a test photo url for this
        //r.setPhoto();
        //assertEquals(r.getTitle(), "Test");

        r.setServings(1);
        assertEquals(r.getServings(), 1);
    }

    @Test
    public void testRecipeItemAddIngredient() {
        RecipeItem r = new RecipeItem();
        GregorianCalendar gcal = new GregorianCalendar(0, 0, 0);
        IngredientItem i = new IngredientItem(
                "Bacon",
                "pig bacon",
                2,
                "k/g",
                "Meat",
                gcal,
                "photoLink",
                "Fridge"
        );

        r.addIngredient(i);
        assertEquals(i, r.getIngredients().get(0));
    }

    @Test
    public void testRecipeItemDeleteIngredient() {
        RecipeItem r = new RecipeItem();
        ArrayList<IngredientItem> ingredientItems = new ArrayList<IngredientItem>();
        GregorianCalendar gcal = new GregorianCalendar(0, 0, 0);
        ingredientItems.add(new IngredientItem(
                "Bacon",
                "pig bacon",
                2,
                "k/g",
                "Meat",
                gcal,
                "photoLink",
                "Fridge"
        ));
        r.setIngredients(ingredientItems);
        assertEquals(ingredientItems, r.getIngredients());

        r.deleteIngredient(ingredientItems.get(0));
        assertEquals(0, r.getIngredients().size());


    }

    @Test
    public void testRecipeItemDeleteIngredientIndex() {
        RecipeItem r = new RecipeItem();
        ArrayList<IngredientItem> ingredientItems = new ArrayList<IngredientItem>();
        GregorianCalendar gcal = new GregorianCalendar(0, 0, 0);
        ingredientItems.add(new IngredientItem(
                "Bacon",
                "pig bacon",
                2,
                "k/g",
                "Meat",
                gcal,
                "photoLink",
                "Fridge"
        ));
        r.setIngredients(ingredientItems);
        assertEquals(ingredientItems, r.getIngredients());

        r.deleteIngredient(0);
        assertEquals(0, r.getIngredients().size());
    }

}