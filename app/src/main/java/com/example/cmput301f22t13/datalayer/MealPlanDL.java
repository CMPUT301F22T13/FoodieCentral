package com.example.cmput301f22t13.datalayer;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;
import com.example.cmput301f22t13.uilayer.mealplanstorage.MealPlanArrayAdapter;
import com.example.cmput301f22t13.uilayer.userlogin.ResultListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

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
        populateOnStartup();
    }

    /** populateIngredientsOnStartup - called when first instance of IngredientDL is made
     * listens for db changes and updates the ingredient storage accordingly
     * */
    private void populateOnStartup() {
        CollectionReference getIngredients = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("MealPlan Storage");

        getIngredients.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                mealPlanStorage.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String hash = doc.getId();
                    GregorianCalendar startDate = new GregorianCalendar();
                    GregorianCalendar endDate = new GregorianCalendar();

                    try {
                        startDate.setTimeInMillis(doc.getDouble("Start Date").longValue());

                    } catch (Exception e) {}
                    try {
                        endDate.setTimeInMillis(doc.getDouble("End Date").longValue());
                    } catch (Exception e) {}



                    MealPlan m = new MealPlan();
                    m.setStartDate(startDate);
                    m.setEndDate(endDate);
                    m.setHashId(hash);
                    mealPlanStorage.add(m);
                }
                listener.onSuccess();
            }
        });
    }

    /** Add/Edit item recieved from Domain Layer into FireStore Ingredient storage collection
     * @Input: IngredientItem item - item to add or edit
     * */
    public void firebaseAddEdit(MealPlan item) {
        //Initializing data value from item object
        GregorianCalendar startDate = item.getStartDate();
        GregorianCalendar endDate = item.getEndDate();



        //Storing data collected from object in a HashMap
        Map<String, Object> mealPlanItem = new HashMap<>();
        if (startDate != null)
            mealPlanItem.put("Start Date", startDate.getTimeInMillis()); // ing_bestBefore.get(Calendar.DATE));

        if (endDate != null)
            mealPlanItem.put("End Date", endDate.getTimeInMillis());
        //Storing data in Hashmap to correct location in Firebase using uniqueKey as document reference
        DocumentReference mealPlanStorage = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("MealPlan Storage")
                .document(item.getHashId());

        mealPlanStorage.set(mealPlanItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "firebaseAdd works as wanted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "firebaseAdd does not work");
            }
        });

    }

    /** Getter for ingredient storage
     * @Returns: ArrayList<IngredientItem> representing ingredients in storage
     * */
    public ArrayList<MealPlan> getStorage() {
        return mealPlanStorage;
    }
}
