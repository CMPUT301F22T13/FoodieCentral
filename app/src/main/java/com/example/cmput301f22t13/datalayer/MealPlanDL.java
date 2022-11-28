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
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String hash = doc.getId();
                    GregorianCalendar startDate = new GregorianCalendar();
                    GregorianCalendar endDate = new GregorianCalendar();

                    try {
                        startDate.setTimeInMillis(doc.getDouble("Start Date").longValue());
                    } catch (Exception e) {
                    }
                    try {
                        endDate.setTimeInMillis(doc.getDouble("End Date").longValue());
                    } catch (Exception e) {
                    }

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
                                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                            RecipeItem r = new RecipeItem();

                                            String hash = doc.getId();
                                            String title = doc.getString("Title");
                                            try {
                                                int prep = doc.getDouble("Prep Time").intValue();
                                                int servings = doc.getDouble("Servings").intValue();
                                                r.setPrepTime(prep);
                                                r.setServings(servings);
                                            } catch (Exception e) {
                                            }

                                            String category = doc.getString("Category");
                                            String comments = doc.getString("Comments");
                                            String photo = doc.getString("Photo");


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
                                                                try {
                                                                    amount = (Double) doc.getDouble("Amount");
                                                                } catch (Exception e) {
                                                                }


                                                                IngredientItem i = new IngredientItem();
                                                                i.setName(name);
                                                                i.setDescription(description);
                                                                i.setAmount(amount.intValue());
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
                                            Double amount = 0.0;
                                            try {
                                                bestbefore.setTimeInMillis(doc.getDouble("Best Before").longValue());

                                            } catch (Exception e) { }
                                            try {
                                                amount = (Double) doc.getDouble("Amount");
                                            } catch (Exception e) { }


                                            IngredientItem i = new IngredientItem();
                                            i.setName(name);
                                            i.setDescription(description);
                                            try {
                                                i.setAmount(amount.intValue());
                                            } catch (Exception e) { }

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
                            listener.onSuccess();
                        }
                    });

                }
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


        // Food tings
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

            daysStorage.set(day).addOnSuccessListener(new OnSuccessListener<Void>() {
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

            // Mealplan Items
            for(Item j : i.getValue()) {
                // If recipe set recipe storage
                if (j instanceof RecipeItem) {
                    //Initializing and storing data value from passed in Recipe item
                    String recipe_title = ((RecipeItem) j).getTitle();
                    int recipe_prepTime = ((RecipeItem) j).getPrepTime();
                    int recipe_servings = ((RecipeItem) j).getServings();
                    String recipe_category = ((RecipeItem) j).getCategory();
                    String recipe_comments = ((RecipeItem) j).getComments();
                    String recipe_photo = j.getPhoto();

                    //Storing data collected from object in a HashMap
                    Map<String, Object> recipe = new HashMap<>();
                    recipe.put("Title", recipe_title);
                    recipe.put("Prep Time", recipe_prepTime);
                    recipe.put("Servings", recipe_servings);
                    recipe.put("Category", recipe_category);
                    recipe.put("Comments", recipe_comments);
                    recipe.put("Photo", recipe_photo);

                    //Storing data in Hashmap to correct location in Firebase using uniqueKey as document reference
                    DocumentReference recipeStorage = daysStorage
                            .collection("Recipe Storage")
                            .document(item.getHashId());

                    ArrayList<IngredientItem> ingredientItems = ((RecipeItem) j).getIngredients();

                    for (IngredientItem k: ingredientItems) {
                        Map<String, Object> ingredient = new HashMap<>();
                        ingredient.put("Name", k.getName());
                        ingredient.put("Description", k.getDescription());
                        ingredient.put("Amount",k.getAmount());
                        ingredient.put("Unit", k.getUnit());
                        ingredient.put("Category", k.getCategory());


                        DocumentReference ingredientStorage = daysStorage
                                .collection("Recipe Storage")
                                .document(item.getHashId())
                                .collection("Ingredients")
                                .document(k.getHashId());

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


                } else {
                    Map<String, Object> ingredient = new HashMap<>();
                    ingredient.put("Name", j.getName());
                    ingredient.put("Description", ((IngredientItem) j).getDescription());
                    ingredient.put("Amount", ((IngredientItem) j).getAmount());
                    ingredient.put("Unit", ((IngredientItem) j).getUnit());
                    ingredient.put("Category", ((IngredientItem) j).getCategory());

                    DocumentReference ingredientStorage = daysStorage
                            .collection("Ingredients")
                            .document(j.getHashId());

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

            }
        }

    }

    /** Getter for mealplan storage
     * @Returns: ArrayList<MealPlan> representing mealplans in storage
     * */
    public ArrayList<MealPlan> getStorage() {
        return mealPlanStorage;
    }
}
