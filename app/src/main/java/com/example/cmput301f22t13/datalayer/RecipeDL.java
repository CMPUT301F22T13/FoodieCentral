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

public class RecipeDL extends FireBaseDL {

    /** Add recepie recieved from Domain Layer into FireStore Ingredient storage collection
     * @Input: RecepieItem item - item added is an ingredient item,
     *         String uniqueKey - A unique key string to store ingredient item in a unique Firestore document
     * */

    public void recepiesFirebaseAdd(RecipeItem item, String uniqueKey) {

        //Initializing data value from item object
        //TODO figure out how to store the array of ingredient items - are those coming from ingredient storage?
        auth = FirebaseAuth.getInstance();
        String recepie_title = item.getTitle();
        Integer recepie_prepTime = item.getPrepTime();
        Integer recepie_servings = item.getServings();
        String recepie_category = item.getCategory();
        String recepie_comments = item.getComments();
        String recepie_photo = item.getPhoto();
        ArrayList<IngredientItem> ingredientItems = item.getIngredients();

        //Storing data collected from object in a HashMap


        Map<String, Object> ingredients = new HashMap<>();
        ingredients.put("Title", recepie_title);
        ingredients.put("Prep Time", recepie_prepTime);
        ingredients.put("Servings", recepie_servings);
        ingredients.put("Category", recepie_category);
        ingredients.put("Comments", recepie_comments);
        ingredients.put("Photo", recepie_photo);
        //Storing ArrayList of ingredients

        //Storing data in Hashmap to correct location in Firebase using uniqueKey as document reference
        DocumentReference recepieStorage = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Recepie Storage")
                .document(uniqueKey);

        //onSucessListener to validate task completion
        recepieStorage.set(ingredientItems).addOnSuccessListener(new OnSuccessListener<Void>() {
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


    /** Gets values from FireStore for perticular recepie and returns an RecepieItem object
     * @Input: uniqueKey - A unique key string to store ingredient item in a unique Firestore document
     * */
    public void ingrideintFirebaseGet(String uniqueKey) {

        //Referencing wanted document from correct location in Firestore database
        DocumentReference getRecepies= fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Recepie Storage")
                .document(uniqueKey);

        //Getting contents of the document and assigning an onSuccessLister to validate task completion
        getRecepies.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            //If document is not empty then store values in IngredientItem object
                            //TODO discuss with team if they want result in ArrayList (show output) OR in IngredientItem object
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
                                //TODO need to work with RecepieItem to initilize all recepie feilds as Strings
                                //TODO need to figure out how to get ingredient items
                                //Need to wrap up
//                                RecipeItem result = new RecipeItem(
//                                        document.getString("Title"),
//                                        document.getString("Category"),
//                                        document.getString("Comments"),
//                                        document.getString("Photo")
//
//                                )
                                //document.getString("Prep Time"), document.getString("Servings")



                                //TODO need to have a callback to the recepieDL to update ingredient storage
                                //Need to have a callback to have recepieDl update storage

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



}
