package com.example.cmput301f22t13.domainlayer.storage;

import android.util.Log;

import com.example.cmput301f22t13.datalayer.FireBaseDL;
import com.example.cmput301f22t13.datalayer.IngredientDL;
import com.example.cmput301f22t13.domainlayer.item.IngredientItem;

import java.util.ArrayList;

public class IngredientStorage {

    private static final String TAG = "IngredientStorageDL";
    /** ArrayList of IngredientItems representing the ingredients in storage
     * */
    private ArrayList<IngredientItem> ingredients = new ArrayList<IngredientItem>();

    /** Constructor for IngredientStorage
     * gets ingredients from DL
     * */
    public IngredientStorage() {
        // TODO: need to write this method - assumed that this will return ingredient items
        //  - if not (returns json objects) then will need to parse in separate function
        // ingredients = IngredientDL.getAllIngredientsInStorage();
    }

    /** Adds ingredient to storage
     * @Input: IngredientItem i - item to add
     * */
    public void addIngredientToStorage(IngredientItem i) {
        try {
            // TODO: Implement IngredientDL.addIngredientToStorage(item)
            // IngredientDL.addIngredientToStorage(item);

            ingredients.add(i);
        } catch (Exception e) {
            Log.e(TAG, "addIngredientToStorage: ", e);
        }
    }

    /** Removes ingredient from storage
     * @Input: IngredientItem item - item to remove
     * */
    public void removeIngredientFromStorage(IngredientItem item) {
        try {
            if (!ingredients.contains(item))
                throw new NullPointerException(); // Think this is the right exception

            // TODO: Implement IngredientDL.removeIngredientFromStorage(item)
            // IngredientDL.removeIngredientFromStorage(item);
            ingredients.remove(item);
        } catch (Exception e) {
            Log.e(TAG, "removeIngredientFromStorage: ", e);
        }
    }

    /** Removes ingredient from storage
     * @Input: int index - index of item to remove
     * */
    public void removeIngredientFromStorage(int index) {
        try {
            if (index > ingredients.size())
                throw new ArrayIndexOutOfBoundsException();

            // TODO: Implement IngredientDL.removeIngredientFromStorage(item)
            //IngredientDL.removeIngredientFromStorage(ingredients.get(index));
            ingredients.remove(index);
        } catch (Exception e) {
            Log.e(TAG, "removeIngredientFromStorage: ", e);
        }

    }

    /** Getter for ingredient storage
     * @Returns: ArrayList<IngredientItem> representing ingredients in storage
     * */
    public ArrayList<IngredientItem> getIngredients() {
        return ingredients;
    }

}
