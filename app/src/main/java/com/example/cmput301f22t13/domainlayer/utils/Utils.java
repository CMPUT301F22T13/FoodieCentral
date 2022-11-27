package com.example.cmput301f22t13.domainlayer.utils;

import android.util.Log;

import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.domainlayer.item.CountedIngredient;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.Item;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
/** Public class for utility functions such as getting a hash
 *
 * */
public class Utils {
    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Stole from github https://stackoverflow.com/questions/17918553/is-it-good-practice-to-put-date-in-hashcode
    /** Function for getting a unique hash
     *  - credits to Peter Lawrey
     *  - used under the Creative Commons Attribution-ShareAlike license
     * */
    private static final AtomicLong TIME_STAMP = new AtomicLong();
    public static String getUniqueHash() {
        long now = System.currentTimeMillis();
        while (true) {
            long last = TIME_STAMP.get();
            if (now <= last)
                now = last + 1;
            if (TIME_STAMP.compareAndSet(last, now))
                return Long.toString(now);
        }
    }

    public static ArrayList<CountedIngredient> populateShoppingList(){

        ArrayList<CountedIngredient> countedIngredients = new ArrayList<>();
        RecipeItem mockRecipe1 = new RecipeItem();
        RecipeItem mockRecipe2 = new RecipeItem();
        RecipeItem mockRecipe3 = new RecipeItem();

        IngredientItem mockIngredient1 = new IngredientItem();
        IngredientItem mockIngredient2 = new IngredientItem();
        IngredientItem mockIngredient3 = new IngredientItem();

        mockIngredient1.setName("Chicken");
        mockIngredient1.setAmount(5);
        mockIngredient2.setName("Broccoli");
        mockIngredient2.setAmount(3);
        mockIngredient3.setName("Rice");
        mockIngredient3.setAmount(10);

        mockRecipe1.setName("Chicken and Rice");
        mockRecipe1.addIngredient(mockIngredient1);
        mockRecipe1.addIngredient(mockIngredient3);

        mockRecipe2.setName("Chicken, Broccoli and Rice");
        mockRecipe2.addIngredient(mockIngredient1);
        mockRecipe2.addIngredient(mockIngredient2);
        mockRecipe2.addIngredient(mockIngredient3);

        mockRecipe3.setName("Chicken and Broccoli");
        mockRecipe3.addIngredient(mockIngredient1);
        mockRecipe3.addIngredient(mockIngredient2);

        GregorianCalendar startDate = new GregorianCalendar(2022, 11, 18);
        GregorianCalendar endDate = new GregorianCalendar(2022, 11, 30);

        ArrayList<Item> recipes = new ArrayList<>();
        recipes.add(mockRecipe1);
        recipes.add(mockRecipe2);
        recipes.add(mockRecipe3);

        ArrayList<Item> ingredients = new ArrayList<>();
        ingredients.add(mockIngredient1);
        ingredients.add(mockIngredient2);

        TreeMap<GregorianCalendar, ArrayList<Item>> datedRecipes = new TreeMap<>();
        datedRecipes.put(new GregorianCalendar(2022, 11, 18), recipes);
        datedRecipes.put(new GregorianCalendar(2022, 11, 19), ingredients);

        MealPlan mealPlan = new MealPlan(startDate, endDate, datedRecipes);

        TreeMap<GregorianCalendar, ArrayList<Item>> mealPlanItems = mealPlan.getMealPlanItems();
        Collection<ArrayList<Item>> allMealPlanItems = mealPlanItems.values();
        HashMap<String, Integer> mealPlanIngredients = new HashMap<>();
        for (ArrayList<Item> itemList : allMealPlanItems) {
            for (Item item : itemList) {
                if (item instanceof RecipeItem) {
                    for (IngredientItem ingredient : ((RecipeItem) item).getIngredients()) {
                        if (mealPlanIngredients.containsKey(ingredient.getName().toLowerCase())) {
                            int currentIngredientCount = mealPlanIngredients.get(ingredient.getName().toLowerCase());
                            int newAmount = currentIngredientCount + currentIngredientCount;
                            mealPlanIngredients.put(ingredient.getName().toLowerCase(), newAmount);
                        } else {
                            mealPlanIngredients.put(ingredient.getName().toLowerCase(), ingredient.getAmount());
                        }
                    }
                } else if (item instanceof IngredientItem) {
                    if (mealPlanIngredients.containsKey(item.getName().toLowerCase())) {
                        int currentIngredientCount = mealPlanIngredients.get(item.getName().toLowerCase());
                        int newAmount = currentIngredientCount + currentIngredientCount;
                        mealPlanIngredients.put(item.getName().toLowerCase(), newAmount);
                    } else {
                        mealPlanIngredients.put(item.getName().toLowerCase(), ((IngredientItem) item).getAmount());
                    }
                }
            }
        }

        ArrayList<IngredientItem> storedIngredientsDL = IngredientDL.getInstance().getStorage();
        HashMap<String, Integer> storedIngredient = new HashMap<>();
        for (IngredientItem ingredientItem : storedIngredientsDL) {
            storedIngredient.put(ingredientItem.getName().toLowerCase(), ingredientItem.getAmount());
        }
        for (String ingredientName : mealPlanIngredients.keySet()) {
            if (storedIngredient.containsKey(ingredientName)) {
                int neededIngredients = mealPlanIngredients.get(ingredientName) - storedIngredient.get(ingredientName);
                CountedIngredient countedIngredient = new CountedIngredient();
                countedIngredient.setName(ingredientName);
                countedIngredient.setCount(neededIngredients);
                countedIngredients.add(countedIngredient);
            } else {
                CountedIngredient countedIngredient = new CountedIngredient();
                countedIngredient.setName(ingredientName);
                countedIngredient.setCount(mealPlanIngredients.get(ingredientName));
                countedIngredients.add(countedIngredient);
            }
        }

        return countedIngredients;
    }
}
