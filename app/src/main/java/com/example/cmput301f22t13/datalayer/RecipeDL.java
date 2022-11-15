package com.example.cmput301f22t13.datalayer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
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
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/** Will become Singleton class in the future - is responsible for tasks related to adding,deleting,getting and updating Ingredient items
 * */

public class RecipeDL {
    static private RecipeDL recipeDL;
    private static FireBaseDL fb;

    /** Listens for changes to the arraylist
     * */
    public static ResultListener listener;

    /** Stores ingredients
     * */
    public static ArrayList<RecipeItem> recipeStorage = new ArrayList<RecipeItem>();

    /** Gets or creates current instance of the firebase DL
     * */
    public static RecipeDL getInstance(){
        if(recipeDL==null){
            recipeDL = new RecipeDL();
            fb = FireBaseDL.getFirebaseDL();
            // Populate ingredients here
            populateRecipesOnStartup();

        }
        return recipeDL;
    }

    /** populateRecipesOnStartup - called when first instance of IngredientDL is made
     * listens for db changes and updates the ingredient storage accordingly
     * */
    private static void populateRecipesOnStartup() {
        CollectionReference getRecipes = fb.fstore.collection("Users")
                .document(fb.auth.getCurrentUser().getUid())
                .collection("Recipe Storage");

        getRecipes.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                recipeStorage.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    // TODO set all recipe dataums
                    String hash = doc.getId();
                    String title = doc.getString("Title");
                    int prep = doc.getDouble("Prep Time").intValue();
                    int servings = doc.getDouble("Servings").intValue();
                    String category = doc.getString("Category");
                    String comments = doc.getString("Comments");

                    RecipeItem r = new RecipeItem();

                    r.setTitle(title);
                    r.setHashId(hash);
                    r.setPrepTime(prep);
                    r.setServings(servings);
                    r.setCategory(category);
                    r.setComments(comments);


                    CollectionReference getIngredients = fb.fstore.collection("Users")
                            .document(fb.auth.getCurrentUser().getUid())
                            .collection("Recipe Storage").document(r.getHashId())
                            .collection("Ingredients");


                    recipeStorage.add(r);
                }
                listener.onSuccess();
            }
        });
    }


    /** Add/Edit item recieved from Domain Layer into FireStore Ingredient storage collection
     * @Input: IngredientItem item - item to add or edit
     * */
    public void recipeFirebaseAddEdit(RecipeItem item) {
//        IngredientItem test = new IngredientItem();
//
//        test.setName("test");
//        item.addIngredient(test);

        //Initializing and storing data value from passed in Recipe item
        String recipe_title = item.getTitle();
        int recipe_prepTime = item.getPrepTime();
        int recipe_servings = item.getServings();
        String recipe_category = item.getCategory();
        String recipe_comments = item.getComments();
        String recipe_photo = item.getPhoto();


        //Storing data collected from object in a HashMap
        Map<String, Object> recipe = new HashMap<>();
        recipe.put("Title", recipe_title);
        recipe.put("Prep Time", recipe_prepTime);
        recipe.put("Servings", recipe_servings);
        recipe.put("Category", recipe_category);
        recipe.put("Comments", recipe_comments);
        recipe.put("Photo", recipe_photo);


        //Storing data in Hashmap to correct location in Firebase using uniqueKey as document reference
        DocumentReference recipeStorage = fb.fstore.collection("Users")
                .document(fb.auth.getCurrentUser().getUid())
                .collection("Recipe Storage")
                .document(item.getHashId());


        for (IngredientItem i: item.getIngredients()) {
            Map<String, Object> ingredient = new HashMap<>();
            ingredient.put("Name", i.getName());
            ingredient.put("Description", i.getDescription());
            ingredient.put("Amount",i.getAmount());
            ingredient.put("Unit", i.getUnit());
            ingredient.put("Category", i.getCategory());

            DocumentReference ingredientStorage = fb.fstore.collection("Users")
                    .document(fb.auth.getCurrentUser().getUid())
                    .collection("Recipe Storage")
                    .document(item.getHashId())
                    .collection("Ingredients")
                    .document(i.getHashId());

            ingredientStorage.set(ingredient).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        recipeStorage.set(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
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


    /** Deletes data from Firestore for a particular passed in Recipe item - by doing so the recipe document is deleted from Firestore
     *  @param item - Recipe item to be deleted from Firestore
     * */
    public void recipeFirebaseDelete(RecipeItem item){
        //Referencing wanted document from correct location in Firestore database
        DocumentReference deleteIngredient = fb.fstore.collection("Users")
                .document(fb.auth.getCurrentUser().getUid())
                .collection("Recipe Storage")
                .document(item.getHashId());

        deleteIngredient.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("tag", "Recipe item successfully deleted from Firebase");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Recipe item not deleted");
            }
        });
    }

    /** Getter for ingredient storage
     * @Returns: ArrayList<IngredientItem> representing ingredients in storage
     * */
    public ArrayList<RecipeItem> getRecipes() {
        return recipeStorage;
    }

}
