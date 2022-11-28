package com.example.cmput301f22t13.datalayer;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.example.cmput301f22t13.uilayer.userlogin.ResultListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


/** Singleton class - is responsible for tasks related to adding,deleting,getting and
 *  updating Ingredient items
 *  @extends: FirebaseDL
 * */
public class IngredientDL extends FireBaseDL {
    static private IngredientDL ingredientDL;

    /** Listens for changes to the arraylist
     * */
    public static ResultListener listener;

    /** Stores ingredients
     * */
    public static ArrayList<IngredientItem> ingredientStorage = new ArrayList<IngredientItem>();
    private ListenerRegistration registration;

    /** Gets or creates current instance of the firebase DL
     * */
    public static IngredientDL getInstance(){
        if(ingredientDL==null){
            ingredientDL = new IngredientDL();
        }
        return ingredientDL;
    }

    /** Constructor calls populateOnStartup
     *  */
    public IngredientDL() {
        // Populate ingredients here
        populateOnStartup();
    }

    public void deRegisterListener(){
        registration.remove();
    }

    /** populateIngredientsOnStartup - called when first instance of IngredientDL is made
     * listens for db changes and updates the ingredient storage accordingly
     * */
    public void populateOnStartup() {
        CollectionReference getIngredients = fstore.collection("Users")
        .document(auth.getCurrentUser().getUid())
        .collection("Ingredient Storage");
        registration = getIngredients.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredientStorage.clear();
                if(queryDocumentSnapshots!=null) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String hash = doc.getId();
                        String name = doc.getString("Name");
                        String description = (String) doc.getData().get("Description");
                        String unit = (String) doc.getData().get("Unit");
                        String category = (String) doc.getData().get("Category");
                        String location = (String) doc.getData().get("Location");
                        String photo = doc.getString("Photo");
                        GregorianCalendar bestbefore = new GregorianCalendar();
                        Double amount = doc.getDouble("Amount");

                        try {
                            bestbefore.setTimeInMillis(doc.getDouble("Best Before").longValue());
                        } catch (Exception e) {}


                        IngredientItem i = new IngredientItem();
                        i.setName(name);
                        i.setDescription(description);
                        i.setAmount(amount);
                        i.setUnit(unit);
                        i.setCategory(category);
                        i.setLocation(location);
                        i.setHashId(hash);
                        i.setBbd(bestbefore);
                        i.setPhoto(photo);
                        ingredientStorage.add(i);
                    }
                }
                if (listener != null) {
                    listener.onSuccess();
                }
            }
        });
    }


    /** Add/Edit item recieved from Domain Layer into FireStore Ingredient storage collection
     * @Input: IngredientItem item - item to add or edit
     * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void firebaseAddEdit(IngredientItem item) {
        //Storing data in Hashmap to correct location in Firebase using uniqueKey as document reference
        DocumentReference ingredientStorage = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(item.getHashId());

        addToFireBase(getIngredientMap(item), ingredientStorage);
    }


    /** Deletes data from Firestore for a particular passed in ingredient item - by doing so the ingredient document is deleted from Firestore
     *  @param item - Ingredient item to be deleted from Firestore
     * */
    public void firebaseDelete(IngredientItem item){
        //Referencing wanted document from correct location in Firestore database
        DocumentReference deleteIngredient = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Ingredient Storage")
                .document(item.getHashId());
        
        deleteFromFireBase(deleteIngredient);
    }

    /** Getter for ingredient storage
     * @Returns: ArrayList<IngredientItem> representing ingredients in storage
     * */
    public ArrayList<IngredientItem> getStorage() {
        return ingredientStorage;
    }


    public static Map<String, Object>  getIngredientMap(IngredientItem item) {
        //Initializing and storing data value from passed in Recipe item
        String ing_name = item.getName();
        String ing_description = item.getDescription();
        GregorianCalendar ing_bestBefore = item.getBbd();
        String ing_location = item.getLocation();
        Double ing_amount = item.getAmount();
        String ing_unit = item.getUnit();
        String ing_category = item.getCategory();
        String ing_photo = item.getPhoto();

        //Storing data collected from object in a HashMap
        Map<String, Object> ingredient = new HashMap<>();
        ingredient.put("Name", ing_name);
        ingredient.put("Description", ing_description);
        if (ing_bestBefore != null)
            ingredient.put("Best Before", ing_bestBefore.getTimeInMillis()); // ing_bestBefore.get(Calendar.DATE));
        ingredient.put("Location", ing_location);
        ingredient.put("Amount", ing_amount);
        ingredient.put("Unit", ing_unit);
        ingredient.put("Category", ing_category);
        ingredient.put("Photo", ing_photo);

        return ingredient;
    }

    public static Map<String, Object>  getRecipeIngredientMap(IngredientItem item) {
        //Initializing and storing data value from passed in Recipe item
        Map<String, Object> ingredient = new HashMap<>();
        ingredient.put("Name", item.getName());
        ingredient.put("Description", item.getDescription());
        ingredient.put("Amount",item.getAmount());
        ingredient.put("Unit", item.getUnit());
        ingredient.put("Category", item.getCategory());

        return ingredient;
    }





}

