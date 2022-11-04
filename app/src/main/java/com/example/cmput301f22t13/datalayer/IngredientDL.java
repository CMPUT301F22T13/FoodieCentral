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

public class IngredientDL extends FireBaseDL {

    /** Add item recieved from Domain Layer into FireStore Ingredient storage collection
     * @Input: IngredientItem item - item added is an ingredient item,
     *         String uniqueKey - A unique key string to store ingredient item in a unique Firestore document
     * */
    @RequiresApi(api = Build.VERSION_CODES.N)

    public void ingridientFirebaseAdd(IngredientItem item, String hashId) {

        //Initializing data value from item object

        auth = FirebaseAuth.getInstance();
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

        //Storing data in Hashmap to correct location in Firebase using uniqueKey as document reference
        DocumentReference ingredientStorage = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(hashId);

        //onSucessListener to validate task completion
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


    /** Gets values from FireStore for perticular ingredient and returns an IngredientItem object
     * @Input: uniqueKey - A unique key string to store ingredient item in a unique Firestore document
     * */
    public void ingrideintFirebaseGet(String hashId) {

        //Referencing wanted document from correct location in Firestore database
        DocumentReference getIngredients = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(hashId);

        //Getting contents of the document and assigning an onSuccessLister to validate task completion
        getIngredients.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            //If document is not empty then store values in IngredientItem object

                            if (document.exists()) {
//                                ArrayList<String> ingredientId = new ArrayList<>();
//                                //Adding to ingredients arrayList
//                                ingredientId.add(document.getString("Name"));
//                                ingredientId.add(document.getString("Description"));
//                                ingredientId.add(document.getString("Best Before"));
//                                ingredientId.add(document.getString("Location"));
//                                ingredientId.add(document.getString("Amount"));
//                                ingredientId.add(document.getString("Unit"));
//                                ingredientId.add(document.getString("Category"));
//                                ingredientId.add(document.getString("Image"));


                                Map<String,Object> ingredientMap = document.getData();








//                                ArrayList<String> userEmail;
//                                userEmail = new ArrayList<>();
//                                IngredientItem result = new IngredientItem(
////                                        document.get("Name"),
////                                        document.get("Description"),
////                                        document.get("Best before"),
////                                        document.get("Location"),
////                                        document.get("Amount"),
////                                        document.get("Unit"),
////                                        document.get("Category"),
////                                        document.get("Image")
//
//                                );




                                //TODO need to have a callback to the ingredientDL to update ingredient storage


                                //Need to have a callback to have ingreidentDl update storage


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


    /** Update values from FireStore for perticular ingredient
     * @Input:  IngredientItem item - item added is an ingredient item,
     *          uniqueKey - A unique key string to store ingredient item in a unique Firestore document
     * */

    public void ingrideintFirebaseUpdatet(IngredientItem item, String hashId){
        //Referencing wanted document from correct location in Firestore database
        DocumentReference updateIngredients = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(hashId);


        //Updating all the ingredient value irregardless of if values have changed or not
        //TODO need to intilize Best Before, Location and Image instances in IngredientItem and have getters and setters for them

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

    /** Deletes ingredient from FireStore
     * @Input:  IngredientItem item - item added is an ingredient item,
     *          uniqueKey - A unique key string to store ingredient item in a unique Firestore document
     * */

    public void ingredientFirebaseDelete(IngredientItem item, String hashId){
        //Referencing wanted document from correct location in Firestore database
        DocumentReference deleteIngredient = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(hashId);

        //Delete ingredient document from Firebase, adding an onSuccessListener & onFailureListener
        // to signify valid task completion
        deleteIngredient.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("tag", "Ingredient item succesfully deleted from Firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Ingredient item not deleted");
            }
        });
    }




}

