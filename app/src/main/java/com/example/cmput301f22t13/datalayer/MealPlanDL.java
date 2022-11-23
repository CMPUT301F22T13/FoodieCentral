package com.example.cmput301f22t13.datalayer;

import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;
import com.example.cmput301f22t13.uilayer.userlogin.ResultListener;

import java.util.ArrayList;

/**
 * This data layer is yet to be implemented. In this section we will handle all the interactions related to the MealplanDL.
 * The actions performed in this segment would be interacting with the recipe storage and ingredient storage collections in Firestore.
 * This class will be pivitol as it will query data from Firestore to make meal plans for users
 */



public class MealPlanDL extends FireBaseDL {
    static private MealPlanDL mealPlanDL;

    /** Listens for changes to the arraylist
     * */
    public static ResultListener listener;

    /** Stores ingredients
     * */
    public static ArrayList<MealPlan> mealPlanStorage = new ArrayList<MealPlan>();

    /** Gets or creates current instance of the firebase DL
     * */
    public static MealPlanDL getInstance(){
        if(mealPlanDL==null){
            mealPlanDL = new MealPlanDL();
        }
        return mealPlanDL;
    }

    public MealPlanDL() {
        // Populate ingredients here
//        populateOnStartup();
    }
}
