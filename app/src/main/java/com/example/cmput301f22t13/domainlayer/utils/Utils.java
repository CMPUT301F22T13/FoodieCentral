package com.example.cmput301f22t13.domainlayer.utils;

import android.util.Log;

import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.datalayer.MealPlanDL;
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
import java.util.Iterator;
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

    /**
     * Function to populate shopping list
     *
     * @return: ArrayList<CountedIngredient>: Ingredients to show in shopping list
     * */
    public static ArrayList<CountedIngredient> populateShoppingList(){

        ArrayList<CountedIngredient> countedIngredients = new ArrayList<>();

        ArrayList<MealPlan> mealPlans = MealPlanDL.getInstance().getStorage();

        if (mealPlans.size() == 0) {
            return countedIngredients;
        }

        MealPlan mealPlan = mealPlans.get(0);
        GregorianCalendar earliestStartDate = mealPlans.get(0).getStartDate();
        for (MealPlan mealPlanItem : mealPlans) {
            GregorianCalendar currentStartDate = mealPlanItem.getStartDate();
            if (currentStartDate.before(earliestStartDate)) {
                earliestStartDate = currentStartDate;
                mealPlan = mealPlanItem;
            }
        }

        TreeMap<GregorianCalendar, ArrayList<Item>> mealPlanItems = mealPlan.getMealPlanItems();
        Collection<ArrayList<Item>> allMealPlanItems = mealPlanItems.values();
        HashMap<String, Double> mealPlanIngredients = new HashMap<>();
        for (ArrayList<Item> itemList : allMealPlanItems) {
            for (Item item : itemList) {
                if (item instanceof RecipeItem) {
                    for (IngredientItem ingredient : ((RecipeItem) item).getIngredients()) {
                        if (mealPlanIngredients.containsKey(ingredient.getName())) {
                            double currentIngredientCount = mealPlanIngredients.get(ingredient.getName());
                            double newAmount = currentIngredientCount + currentIngredientCount;
                            mealPlanIngredients.put(ingredient.getName(), newAmount);
                        } else {
                            mealPlanIngredients.put(ingredient.getName(), ingredient.getAmount());
                        }
                    }
                } else if (item instanceof IngredientItem) {
                    if (mealPlanIngredients.containsKey(item.getName())) {
                        double currentIngredientCount = mealPlanIngredients.get(item.getName());
                        double newAmount = currentIngredientCount + currentIngredientCount;
                        mealPlanIngredients.put(item.getName(), newAmount);
                    } else {
                        mealPlanIngredients.put(item.getName(), ((IngredientItem) item).getAmount());
                    }
                }
            }
        }

        ArrayList<IngredientItem> storedIngredientsDL = IngredientDL.getInstance().getStorage();
        HashMap<String, IngredientItem> storedIngredientItem = new HashMap<>();
        HashMap<String, Double> storedIngredient = new HashMap<>();
        HashMap<String, String> storedIngredientHashes = new HashMap<>();
        for (IngredientItem ingredientItem : storedIngredientsDL) {
            storedIngredientItem.put(ingredientItem.getName(), ingredientItem);
            storedIngredient.put(ingredientItem.getName(), ingredientItem.getAmount());
            storedIngredientHashes.put(ingredientItem.getName(), ingredientItem.getHashId());
        }
        for (String ingredientName : mealPlanIngredients.keySet()) {
            if (storedIngredient.containsKey(ingredientName)) {
                double neededIngredients = mealPlanIngredients.get(ingredientName) - storedIngredient.get(ingredientName);
                CountedIngredient countedIngredient = new CountedIngredient();
                countedIngredient.setName(ingredientName);
                countedIngredient.setCount(neededIngredients);
                countedIngredient.setHashId(storedIngredientHashes.get(ingredientName));
                countedIngredient.setUnit(storedIngredientItem.get(ingredientName).getUnit());
                countedIngredient.setBbd(storedIngredientItem.get(ingredientName).getBbd());
                countedIngredient.setCategory(storedIngredientItem.get(ingredientName).getCategory());
                countedIngredient.setPhoto(storedIngredientItem.get(ingredientName).getPhoto());
                countedIngredient.setDescription(storedIngredientItem.get(ingredientName).getDescription());
                countedIngredient.setLocation(storedIngredientItem.get(ingredientName).getLocation());
                countedIngredients.add(countedIngredient);
            } else {
                CountedIngredient countedIngredient = new CountedIngredient();
                countedIngredient.setName(ingredientName);
                countedIngredient.setCount(mealPlanIngredients.get(ingredientName));
                countedIngredients.add(countedIngredient);
            }
        }

        Iterator<CountedIngredient> itr = countedIngredients.iterator();
        while (itr.hasNext()) {
            CountedIngredient ingredient = itr.next();
            if (ingredient.getCount() <= 0) {
                itr.remove();
            }
        }

        return countedIngredients;
    }
}
