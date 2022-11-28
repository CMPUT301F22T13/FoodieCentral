package com.example.cmput301f22t13.datalayer;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.cmput301f22t13.domainlayer.item.IngredientItem;
import com.example.cmput301f22t13.domainlayer.item.Item;
import com.example.cmput301f22t13.domainlayer.item.MealPlan;
import com.example.cmput301f22t13.domainlayer.item.RecipeItem;
import com.example.cmput301f22t13.uilayer.mealplanstorage.MealPlanArrayAdapter;
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
 *  updating MealPlan items
 *  @extends: FirebaseDL
 * */
public class MealPlanDL extends FireBaseDL {
    static private MealPlanDL mealPlanDL;

    /** Listens for changes to the arraylist
     * */
    public static ResultListener listener;

    /** Stores mealplans
     * */
    public static ArrayList<MealPlan> mealPlanStorage = new ArrayList<MealPlan>();
    private ListenerRegistration registration;

    /** Gets or creates current instance of the MealPlanDL
     * */
    public static MealPlanDL getInstance(){
        if(mealPlanDL==null){
            mealPlanDL = new MealPlanDL();
        }
        return mealPlanDL;
    }

    /** Constructor calls populateOnStartup
     *  */
    public MealPlanDL() {
        // Populate mealplans here
        populateOnStartup();
    }

    public void deRegisterListener(){
        registration.remove();
    }

    /** populateOnStartup - called when first instance of MealPlanDL is made
     * listens for db changes and updates the mealplan storage accordingly
     * */
    private void populateOnStartup() {
        registration = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("MealPlan Storage")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                            FirebaseFirestoreException error) {
                        mealPlanStorage.clear();
                    if (queryDocumentSnapshots != null) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String hash = doc.getId();
                            GregorianCalendar startDate = new GregorianCalendar();
                            GregorianCalendar endDate = new GregorianCalendar();

                            try {
                                startDate.setTimeInMillis(doc.getDouble("Start Date").longValue());
                            } catch (Exception e) { }
                            try {
                                endDate.setTimeInMillis(doc.getDouble("End Date").longValue());
                            } catch (Exception e) { }

                            MealPlan m = new MealPlan(startDate, endDate, doc.getId());

                            CollectionReference days = fstore.collection("Users")
                                    .document(auth.getCurrentUser().getUid())
                                    .collection("MealPlan Storage")
                                    .document(hash)
                                    .collection("Days");

                            days.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                        GregorianCalendar date = new GregorianCalendar();
                                        date.setTimeInMillis(doc.getDouble("Date").longValue());

                                        days.document(doc.getId()).collection("Recipe Storage").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot doc2 : queryDocumentSnapshots) {
                                                    RecipeItem r = new RecipeItem();

                                                    String hash = doc2.getId();
                                                    String title = doc2.getString("Title");
                                                    int prep = doc2.getDouble("Prep Time").intValue();
                                                    int servings = doc2.getDouble("Servings").intValue();
                                                    r.setPrepTime(prep);
                                                    r.setServings(servings);

                                                    String category = doc2.getString("Category");
                                                    String comments = doc2.getString("Comments");
                                                    String photo = doc2.getString("Photo");

                                                    r.setTitle(title);
                                                    r.setHashId(hash);
                                                    r.setCategory(category);
                                                    r.setComments(comments);
                                                    r.setPhoto(photo);

                                                    days.document(doc.getId())
                                                            .collection("Recipe Storage").document(r.getHashId())
                                                            .collection("Ingredients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                                                        String hash = doc.getId();
                                                                        String name = doc.getString("Name");
                                                                        String description = (String) doc.getData().get("Description");
                                                                        String unit = (String) doc.getData().get("Unit");
                                                                        String category = (String) doc.getData().get("Category");
                                                                        String photo = doc.getString("Photo");
                                                                        Double amount = 0.0;
                                                                        amount = (Double) doc.getDouble("Amount");

                                                                        IngredientItem i = new IngredientItem();
                                                                        i.setName(name);
                                                                        i.setDescription(description);
                                                                        i.setAmount(amount);
                                                                        i.setUnit(unit);
                                                                        i.setCategory(category);
                                                                        i.setHashId(hash);
                                                                        i.setPhoto(photo);
                                                                        r.addIngredient(i);
                                                                    }
                                                                }
                                                            });

                                                    m.addItemForDay(date, r);
                                                }
                                            }
                                        });

                                        days.document(doc.getId()).collection("Ingredients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                                    String hash = doc.getId();
                                                    String name = doc.getString("Name");
                                                    String description = (String) doc.getData().get("Description");

                                                    String unit = (String) doc.getData().get("Unit");
                                                    String category = (String) doc.getData().get("Category");
                                                    String location = (String) doc.getData().get("Location");
                                                    String photo = doc.getString("Photo");
                                                    GregorianCalendar bestbefore = new GregorianCalendar();
                                                    Double amount = (Double) doc.getDouble("Amount");
                                                    try {
                                                        bestbefore.setTimeInMillis(doc.getDouble("Best Before").longValue());

                                                    } catch (Exception e) { }

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

                                                    m.addItemForDay(date, i);
                                                }
                                            }
                                        });
                                    }
                                    mealPlanStorage.add(m);
                                    if (listener != null) {
                                        listener.onSuccess();
                                    }
                                }
                            });

                        }
                    }
                }
        });
    }

    /** Add/Edit item received from Domain Layer into FireStore MealPlan storage collection
     * @Input: MealPlan item - item to add or edit
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

        addToFireBase(mealPlanItem, mealPlanStorage);

        // Logic area for adding the items
        for (Map.Entry<GregorianCalendar, ArrayList<Item>>
            i : item.getMealPlanItems().entrySet()) {

            Map<String, Object> day = new HashMap<>();
            day.put("Date", i.getKey().getTimeInMillis());
            String hash = String.valueOf(i.getKey().getTimeInMillis());

            DocumentReference daysStorage = fstore.collection("Users")
                    .document(auth.getCurrentUser().getUid())
                    .collection("MealPlan Storage")
                    .document(item.getHashId())
                    .collection("Days")
                    .document(hash);

            addToFireBase(day, daysStorage);

            // Mealplan Items
            for(Item j : i.getValue()) {
                // If recipe set recipe storage
                if (j instanceof RecipeItem) {
                    //Storing data collected from object in a HashMap
                    Map<String, Object> recipe = RecipeDL.getRecipeMap((RecipeItem) j);

                    //Storing data in Hashmap to correct location in Firebase using uniqueKey as document reference
                    DocumentReference recipeStorage = daysStorage
                            .collection("Recipe Storage")
                            .document(j.getHashId());

                    ArrayList<IngredientItem> ingredientItems = ((RecipeItem) j).getIngredients();

                    for (IngredientItem k: ingredientItems) {
                        Map<String, Object> ingredient = IngredientDL.getIngredientMap(k);

                        DocumentReference ingredientStorage = daysStorage
                                .collection("Recipe Storage")
                                .document(j.getHashId())
                                .collection("Ingredients")
                                .document(k.getHashId());

                        addToFireBase(ingredient, ingredientStorage);
                    }
                    addToFireBase(recipe, recipeStorage);

                } else {
                    Map<String, Object> ingredient = IngredientDL.getIngredientMap((IngredientItem) j);

                    DocumentReference ingredientStorage = daysStorage
                            .collection("Ingredients")
                            .document(j.getHashId());

                    addToFireBase(ingredient, ingredientStorage);
                }
            }
        }
    }

    /** Deletes data from Firestore for a particular passed in MealPlan item
     *  @param item -MealPlan item to be deleted from Firestore
     * */
    public void firebaseDelete(MealPlan item){
        //Referencing wanted document from correct location in Firestore database
        DocumentReference doc = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("MealPlan Storage")
                .document(item.getHashId());

        deleteFromFireBase(doc);
    }


    /** Deletes data from Firestore for a particular passed in MealPlan item
     *  @param mealPlanItem - MealPlan item with item to delete
     *  @param item - Ingredient or Recipe item to delete
     * */
    public void deleteItem(MealPlan mealPlanItem, Item item, GregorianCalendar date) {
        String coll = "Ingredients";
        if (item instanceof RecipeItem)
            coll = "Recipe Storage";

        DocumentReference deleteIngredient = fstore.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("MealPlan Storage")
                .document(mealPlanItem.getHashId())
                .collection("Days")
                .document(String.valueOf(date.getTimeInMillis()))
                .collection(coll)
                .document(item.getHashId());

        deleteFromFireBase(deleteIngredient);
    }


    /** Getter for mealplan storage
     * @Returns: ArrayList<MealPlan> representing mealplans in storage
     * */
    public ArrayList<MealPlan> getStorage() {
        return mealPlanStorage;
    }
}
