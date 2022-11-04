package com.example.cmput301f22t13.datalayer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Will become Singleton class in the future - is responsible for tasks related to adding,deleting,getting and updating Ingredient items
 * */

public class RecipeDL extends FireBaseDL {

    /** Add recipe received from Domain Layer into FireStore Recipe storage collection
     * @param  item - item added is an Recipe item
     *@param hashKey - A unique hash for every item that differentiates one item from another. Used for unique recipe storage in Firebase
     *Current status - Incomplete, need to store the arrayList of ingredient items - will be part of our upcoming Sprint plan
     * */

    public void recepiesFirebaseAdd(RecipeItem item, String hashKey) {

        auth = FirebaseAuth.getInstance();
        //Initializing and storing data value from passed in Recipe item
        String recipe_title = item.getTitle();
        Integer recipe_prepTime = item.getPrepTime();
        Integer recipe_servings = item.getServings();
        String recipe_category = item.getCategory();
        String recipe_comments = item.getComments();
        String recipe_photo = item.getPhoto();
        ArrayList<IngredientItem> ingredientItems = item.getIngredients();

        //Storing data collected from object in a HashMap
        Map<String, Object> ingredients = new HashMap<>();
        ingredients.put("Title", recipe_title);
        ingredients.put("Prep Time", recipe_prepTime);
        ingredients.put("Servings", recipe_servings);
        ingredients.put("Category", recipe_category);
        ingredients.put("Comments", recipe_comments);
        ingredients.put("Photo", recipe_photo);
        //TODO implement storage for arrayList of ingredientItems


        //Storing data in Hashmap to correct location in Firebase using hashKey as document reference
        DocumentReference recepieStorage = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Recepie Storage")
                .document(hashKey);

        recepieStorage.set(ingredientItems).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "firebaseAdd works as wanted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "FirebaseAdd does not work");
            }
        });

    }


    /**  Gets data stored from Firestore for a wanted recipe and returns a RecipeItem object
     * @param  hashKey - A unique hash for every recipe that differentiates one item from another - Used to accomplish unique recipe storage in Firebase
     * Current status - Incomplete, need to incorporate query for object retrieval and returning the object to the domain layer - will be part of up coming Sprint plan
     */

    public void ingrideintFirebaseGet(String hashKey) {

        //Referencing wanted document from correct location in Firestore database
        DocumentReference getRecepies= fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Recipe Storage")
                .document(hashKey);

        //Getting contents of the document and assigning an onSuccessLister to validate task completion
        getRecepies.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            //If document is not empty then store values in IngredientItem object
                            if (document.exists()) {
                                    //Initialize recepie getting

                                //TODO need to have a callback to the recepieDL to update ingredient storage

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

    //Delete

    /** Deletes data from Firestore for a particular passed in Recipe item - by doing so the recipe document is deleted from Firestore
     *  @param item - Recipe item to be deleted from Firestore
     *  @param hashKey - A unique key string to store ingredient item in a unique Firestore document
     *  Current status - Complete
     * */

    public void recipeFirebaseDelete(RecipeItem item, String hashKey){
        //Referencing wanted document from correct location in Firestore database
        DocumentReference deleteIngredient = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Recipe Storage")
                .document(hashKey);

        deleteIngredient.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("tag", "Recipe item successfully deleted from Firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Ingredient item not deleted");
            }
        });
    }

    //Update

    /** Updates recipe items in Firestore based on parameters received from the Domain Layer.
     *     *@param  item - item to be updated is a Recipe item
     *     *@param hashKey - A unique hash for every item that differentiates one item from another. Used for unique item reference in Firestore
     *     *Current status - Incomplete, need to incorporate query for ingredient item traversal and update
     * */

    public void recipeFirebaseUpdate(RecipeItem item, String hashId){
        //Referencing wanted document from correct location in Firestore database
        DocumentReference updateIngredients = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Recipe Storage")
                .document(hashId);


        updateIngredients.update("Title", item.getTitle(), "Comments", item.getComments(),
                        "Prep Time", item.getPrepTime(), "Servings", item.getServings(), "Category", item.getCategory(),
                        "Photo", item.getPhoto())


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


}
