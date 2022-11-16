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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** Singleton class - is responsible for tasks related to adding,deleting,getting and updating Ingredient items
 * */
public class IngredientDL extends FireBaseDL {
    static private IngredientDL ingredientDL;
    private static FireBaseDL fb;

    /** Listens for changes to the arraylist
     * */
    public static ResultListener listener;

    /** Stores ingredients
     * */
    public static ArrayList<IngredientItem> ingredientStorage = new ArrayList<IngredientItem>();

    /** Gets or creates current instance of the firebase DL
     * */
    public static IngredientDL getInstance(){
        if(ingredientDL==null){
            ingredientDL = new IngredientDL();
            fb = FireBaseDL.getFirebaseDL();
            // Populate ingredients here
            populateIngredientsOnStartup();

        }
        return ingredientDL;
    }


    /** populateIngredientsOnStartup - called when first instance of IngredientDL is made
     * listens for db changes and updates the ingredient storage accordingly
     * */
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
                    String name = doc.getString("Name");
                    String description = (String) doc.getData().get("Description");

                    String unit = (String) doc.getData().get("Unit");
                    String category = (String) doc.getData().get("Category");
                    String location = (String) doc.getData().get("Location");
                    GregorianCalendar bestbefore = new GregorianCalendar();
                    String image = (String) doc.getData().get("Image");
                    Double amount = 0.0;
                    try {
                        bestbefore.setTime(doc.getDate("Best Before"));
                        amount = (Double) doc.getDouble("Amount");
                    } catch (Exception e) {}


                    IngredientItem i = new IngredientItem();
                    i.setName(name);
                    i.setDescription(description);
                    i.setAmount(amount.intValue());
                    i.setUnit(unit);
                    i.setCategory(category);
                    i.setLocation(location);
                    i.setHashId(hash);
                    i.setBbd(bestbefore);
                    i.setPhoto(image);
                    ingredientStorage.add(i);
                }
                listener.onSuccess();
            }
        });
    }


    /** Add/Edit item recieved from Domain Layer into FireStore Ingredient storage collection
     * @Input: IngredientItem item - item to add or edit
     * */
    @RequiresApi(api = Build.VERSION_CODES.O)
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
            ingredientItems.put("Best Before", ing_bestBefore.toZonedDateTime().toInstant()); // ing_bestBefore.get(Calendar.DATE));
        ingredientItems.put("Location", ing_location);
        ingredientItems.put("Amount", ing_amount);
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

