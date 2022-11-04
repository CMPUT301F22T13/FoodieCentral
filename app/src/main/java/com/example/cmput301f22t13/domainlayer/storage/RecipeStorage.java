package com.example.cmput301f22t13.domainlayer.storage;

import android.util.Log;

import com.example.cmput301f22t13.datalayer.FireBaseDL;
import com.example.cmput301f22t13.datalayer.RecipeDL;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;

import java.util.ArrayList;

/** Will be deleted in next sprint when RecipeDL is refactored
 *
 * */
public class RecipeStorage {
    private static final String TAG = "RecipeStorageDL";
    /** ArrayList of RecipeItems representing the recipes in storage
     * */
    private ArrayList<RecipeItem> recipes = new ArrayList<RecipeItem>();

    /** Constructor for recipes
     * gets recipes from DL
     * */
    public RecipeStorage() {
        // TODO: need to write this method - assumed that this will return Recipe items
        //  - if not (returns json objects) then will need to parse in separate function
        // recipes = RecipeDL.getAllRecipesInStorage();
    }

    /** Adds recipe to storage
     * @Input: RecipeItem i - item to add
     * */
    public void addRecipeToStorage(RecipeItem i) {
        try {
            // TODO: Implement RecipeDL.addRecipeToStorage(item)
            // RecipeDL.addRecipeToStorage(item);

            recipes.add(i);
        } catch (Exception e) {
            Log.e(TAG, "addRecipeToStorage: ", e);
        }
    }

    /** Removes Recipe from storage
     * @Input: RecipeItem item - item to remove
     * */
    public void removeRecipeFromStorage(RecipeItem item) {
        try {
            if (!recipes.contains(item))
                throw new NullPointerException(); // Think this is the right exception

            // TODO: Implement RecipeDL.removeRecipeFromStorage(item)
            // RecipeDL.removeRecipeFromStorage(item);
            recipes.remove(item);
        } catch (Exception e) {
            Log.e(TAG, "removeRecipeFromStorage: ", e);
        }
    }

    /** Removes Recipe from storage
     * @Input: int index - index of item to remove
     * */
    public void removeRecipeFromStorage(int index) {
        try {
            if (index > recipes.size())
                throw new ArrayIndexOutOfBoundsException();

            // TODO: Implement RecipeDL.removeRecipeFromStorage(item)
            //RecipeDL.removeRecipeFromStorage(recipes.get(index));
            recipes.remove(index);
        } catch (Exception e) {
            Log.e(TAG, "removeRecipeFromStorage: ", e);
        }

    }

    /** Getter for recipe storage
     * @Returns: ArrayList<Recipes> representing recipes in storage
     * */
    public ArrayList<RecipeItem> getRecipes() {
        return recipes;
    }
}
