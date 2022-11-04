package com.example.cmput301f22t13.datalayer;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


/** Inherits from the FireBaseDL and is responsible for tasks related to adding,deleting,getting and updating Ingredient items
 * */


public class IngredientDL extends FireBaseDL {


    /** Add recipe received from Domain Layer into FireStore Recipe storage collection
     * @param  item - item added is an ingredient item
     *@param hashKey - A unique hash for every item that differentiates one item from another. Used for unique item storage in Firebase
     *Current status - Complete
     * */

    public void ingridientFirebaseAdd(IngredientItem item, String hashKey) {


        auth = FirebaseAuth.getInstance();
        //Initializing and storing data value from passed in Ingredient item
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
        ingredientItems.put("Best Before", ing_bestBefore.toString());
        ingredientItems.put("Location", ing_location);
        ingredientItems.put("Amount", ing_amount.toString());
        ingredientItems.put("Unit", ing_unit);
        ingredientItems.put("Category", ing_category);
        ingredientItems.put("Image", ing_image);

        //Storing data in Hashmap to correct location in Firebase using hashKey as document reference
        DocumentReference ingredientStorage = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(hashKey);

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

    /** Gets data stored from Firestore for a wanted ingredient and returns a Ingredient Item object
     * @param hashKey - A unique hash for every ingredient that differentiates one ingredient from another - Used to accomplish unique ingredient storage in Firebase
     * Current status - Incomplete, need to incorporate query for object retrieval and returning the object to the domain layer - will be part of up coming Sprint plan
     */
    public void ingrideintFirebaseGet(String hashKey) {

        //Referencing wanted document from correct location in Firestore database
        DocumentReference getIngredients = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(hashKey);

        //Getting contents of the document and assigning an onSuccessLister to validate task completion
        getIngredients.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            //If document is not empty then store values in IngredientItem object

                            if (document.exists()) {


                                Map<String,Object> ingredientMap = document.getData();

                                Log.d("TAG", "onComplete: Got from firebase");
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get function failed with", task.getException());
                        }
                    }
                });
    }


    /** Updates ingredient items in Firestore based on parameters received from the Domain Layer.
     *     *@param  item - item to be updated is an ingredient item
     *     *@param hashKey - A unique hash for every item that differentiates one item from another. Used for unique item refrence in Firestore
     *     *Current status - Complete
     * */

    public void ingrideintFirebaseUpdate(IngredientItem item, String hashId){
        //Referencing wanted document from correct location in Firestore database
        DocumentReference updateIngredients = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(hashId);

        updateIngredients.update("Name", item.getName(), "Description", item.getDescription(),
                        "Amount", item.getAmount(), "Unit", item.getUnit().toString(), "Category", item.getCategory(),
                "Best Before", item.getBbd().toString(), "Location", item.getLocation(), "Image", item.getPhoto())

                //Adding an onSuccessListener & onFailureListener to this event to signify valid task completion
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "Successfully updated Ingredients");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Document update unsuccessful");
                    }
                });
    }

    /** Deletes data from Firestore for a particular passed in ingredient item - by doing so the ingredient document is deleted from Firestore
     *  @param item - Ingredient item to be deleted from Firestore
     *  @param hashKey - A unique key string to store ingredient item in a unique Firestore document
     *  Current status - Complete
     * */

    public void ingredientFirebaseDelete(IngredientItem item, String hashKey){
        //Referencing wanted document from correct location in Firestore database
        DocumentReference deleteIngredient = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(hashKey);

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

}

