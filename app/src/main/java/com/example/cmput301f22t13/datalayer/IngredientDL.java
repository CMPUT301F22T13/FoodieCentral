package com.example.cmput301f22t13.datalayer;

import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.uilayer.ingredientstorage.IngredientListAdapter;
import com.example.cmput301f22t13.uilayer.userlogin.ResultListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** Inherits from the FireBaseDL and is responsible for tasks related to adding,deleting,getting and updating Ingredient items
 * */


public class IngredientDL extends FireBaseDL {
    static private IngredientDL ingredientDL;
    private static FireBaseDL fb;
    public static ResultListener listener;

    public static ArrayList<IngredientItem> ingredientStorage = new ArrayList<IngredientItem>();

    public static IngredientDL getInstance(){
        if(ingredientDL==null){
            ingredientDL = new IngredientDL();
            fb = FireBaseDL.getFirebaseDL();
            // Populate ingredients here
            populateIngredientsOnStartup();

        }
        return ingredientDL;
    }

    private static void populateIngredientsOnStartup() {
        CollectionReference getIngredients = fb.fstore.collection("Users")
        .document(fb.auth.getCurrentUser().getUid())
        .collection("Ingredient Storage");

        getIngredients.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredientStorage.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String hash = doc.getId();
                    String name = (String) doc.getData().get("Name");
                    String description = (String) doc.getData().get("Description");
                    String amount = (String) doc.getData().get("Amount");
                    String unit = (String) doc.getData().get("Unit");
                    String category = (String) doc.getData().get("Category");
                    String location = (String) doc.getData().get("Location");

                    IngredientItem i = new IngredientItem();
                    i.setName(name);
                    i.setDescription(description);
                    i.setAmount(Integer.parseInt(amount));
                    i.setUnit(unit);
                    i.setCategory(category);
                    i.setLocation(location);
                    i.setHashId(hash);
                    ingredientStorage.add(i);
                }
                listener.onSuccess();
            }
        });
    }


    /** Add/Edit item recieved from Domain Layer into FireStore Ingredient storage collection
     * @Input: IngredientItem item - item added is an ingredient item,
     *         String uniqueKey - A unique key string to store ingredient item in a unique Firestore document
     * */
    public void ingredientFirebaseAddEdit(IngredientItem item) {
        //Initializing data value from item object
        String ing_name = item.getName();
        String ing_description = item.getDescription();
        GregorianCalendar ing_bestBefore = item.getBbd();
        String ing_location = item.getLocation();
        Integer ing_amount = item.getAmount();
        String ing_unit = item.getUnit();
        String ing_category = item.getCategory();
        String ing_image = item.getPhoto();


        //Storing data collected from object in a HashMap
        Map<String, Object> ingredientItems = new HashMap<>();
        ingredientItems.put("Name", ing_name);
        ingredientItems.put("Description", ing_description);
        if (ing_bestBefore != null)
            ingredientItems.put("Best Before", ing_bestBefore.toString());
        ingredientItems.put("Location", ing_location);
        ingredientItems.put("Amount", ing_amount.toString());
        ingredientItems.put("Unit", ing_unit);
        ingredientItems.put("Category", ing_category);
        ingredientItems.put("Image", ing_image);

        //Storing data in Hashmap to correct location in Firebase using uniqueKey as document reference
        DocumentReference ingredientStorage = fb.fstore.collection("Users")
                .document(fb.auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(item.getHashId());

        ingredientStorage.set(ingredientItems).addOnSuccessListener(new OnSuccessListener<Void>() {
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


    /** Deletes data from Firestore for a particular passed in ingredient item - by doing so the ingredient document is deleted from Firestore
     *  @param item - Ingredient item to be deleted from Firestore
     *  Current status - Complete
     * */
    public void ingredientFirebaseDelete(IngredientItem item){
        //Referencing wanted document from correct location in Firestore database
        DocumentReference deleteIngredient = fb.fstore.collection("Users")
                .document(fb.auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(item.getHashId());

        deleteIngredient.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("tag", "Ingredient item successfully deleted from Firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Ingredient item not deleted");
            }
        });
    }

    /** Getter for ingredient storage
     * @Returns: ArrayList<IngredientItem> representing ingredients in storage
     * */
    public ArrayList<IngredientItem> getIngredients() {
        return ingredientStorage;
    }




}

